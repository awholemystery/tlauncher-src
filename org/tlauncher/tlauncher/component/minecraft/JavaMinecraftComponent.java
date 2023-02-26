package org.tlauncher.tlauncher.component.minecraft;

import ch.qos.logback.core.joran.action.Action;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.launcher.versions.JavaVersionName;
import org.apache.log4j.Logger;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.controller.JavaMinecraftController;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.DownloadableContainer;
import org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter;
import org.tlauncher.tlauncher.downloader.JVMFileDownloadable;
import org.tlauncher.tlauncher.downloader.RetryDownloadException;
import org.tlauncher.tlauncher.entity.minecraft.JVMFile;
import org.tlauncher.tlauncher.entity.minecraft.JVMManifest;
import org.tlauncher.tlauncher.entity.minecraft.JavaVersionDescription;
import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.login.LoginForm;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tlauncher.util.gson.DownloadUtil;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/minecraft/JavaMinecraftComponent.class */
public class JavaMinecraftComponent extends MinecraftLauncherAssistantWrapper {
    private static final Logger log = Logger.getLogger(JavaMinecraftComponent.class);
    @Inject
    private Gson gson;
    @Inject
    private JavaMinecraftController controller;
    private volatile Map<String, JsonObject> javaConfiguration;
    private final String FILE_META_DELIMITER = "/#//";

    @Override // org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper, org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface
    public void collectInfo(MinecraftLauncher launcher) throws MinecraftException {
        if (Objects.isNull(this.javaConfiguration) && Objects.nonNull(launcher.getVersion())) {
            try {
                this.javaConfiguration = (Map) DownloadUtil.loadObjectByKey("java.configuration", new TypeToken<Map<String, JsonObject>>() { // from class: org.tlauncher.tlauncher.component.minecraft.JavaMinecraftComponent.1
                }.getType(), true);
            } catch (JsonSyntaxException | IOException e) {
                U.log(e);
            }
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantWrapper, org.tlauncher.tlauncher.minecraft.launcher.assitent.MinecraftLauncherAssistantInterface
    public void collectResources(MinecraftLauncher launcher) throws MinecraftException {
        MinecraftJava.CompleteMinecraftJava java = this.controller.getCurrent();
        JavaVersionName javaVersionName = Objects.isNull(launcher.getVersion().getJavaVersion()) ? JavaVersionName.JAVA_8_LEGACY : launcher.getVersion().getJavaVersion();
        if (Objects.isNull(java)) {
            prepareDefaultJava(launcher, javaVersionName);
            log.info("used default java runtime");
        } else {
            log.info("used user java runtime");
            launcher.setJavaDir(new File(OS.appendBootstrapperJvm2(java.getPath())));
            launcher.setJava(java);
        }
        launcher.setJavaVersion(javaVersionName.getMajorVersion().intValue());
        U.log(String.format("Minecraft requires java version: %s, java path: %s", Integer.valueOf(launcher.getJavaVersion()), launcher.getJavaDir()));
    }

    private void prepareDefaultJava(MinecraftLauncher launcher, JavaVersionName javaVersionName) throws MinecraftException {
        boolean macosAndARM = false;
        Configuration c = TLauncher.getInstance().getConfiguration();
        String gpuInfo = c.get("gpu.info.full");
        String osName = OS.CURRENT.name().toLowerCase();
        if (OS.CURRENT.equals(OS.OSX) && Objects.nonNull(gpuInfo) && gpuInfo.contains("Apple M1")) {
            macosAndARM = true;
            osName = osName + "-arm64";
        }
        String key = OS.buildJVMKey();
        Path javaPath = Paths.get(MinecraftUtil.getWorkingDirectory().getAbsolutePath(), "runtime", javaVersionName.getComponent(), osName, javaVersionName.getComponent());
        Path javaSHA1Path = Paths.get(javaPath.getParent().toString(), javaVersionName.getComponent() + ".sha1");
        Path javaVersionPath = Paths.get(javaPath.getParent().toString(), ".version");
        JavaVersionDescription javaVersionDescription = null;
        if (Objects.nonNull(this.javaConfiguration)) {
            JsonObject jsonObject = this.javaConfiguration.get(key);
            if (macosAndARM && Objects.nonNull(this.javaConfiguration.get(key + "-arm64"))) {
                jsonObject = this.javaConfiguration.get(key + "-arm64");
            }
            if (Objects.nonNull(jsonObject)) {
                JsonArray jsonObject2 = jsonObject.getAsJsonArray(javaVersionName.getComponent());
                if (Objects.nonNull(jsonObject2) && jsonObject2.size() != 0) {
                    javaVersionDescription = (JavaVersionDescription) this.gson.fromJson(jsonObject2.get(0), (Class<Object>) JavaVersionDescription.class);
                    String name = javaVersionDescription.getVersion().getName();
                    try {
                        if (Files.exists(javaVersionPath, new LinkOption[0]) && !name.equals(FileUtil.readFile(javaVersionPath.toFile()))) {
                            FileUtil.deleteDirectory(javaPath.getParent().toFile());
                        }
                    } catch (IOException e) {
                        U.log(e);
                    }
                }
            }
        }
        if (Files.exists(javaSHA1Path, new LinkOption[0])) {
            try {
                Optional<MetadataDTO> notProper = Arrays.stream(FileUtil.readFile(javaSHA1Path.toFile()).split(System.lineSeparator())).map(e2 -> {
                    String[] array = e2.split("/#//");
                    MetadataDTO m = new MetadataDTO();
                    m.setPath(array[0].trim());
                    m.setSha1(array[1].trim().split(" ")[0]);
                    return m;
                }).filter(e3 -> {
                    return !Objects.equals(FileUtil.getChecksum(Paths.get(javaPath.toString(), e3.getPath()).toFile()), e3.getSha1());
                }).findAny();
                if (notProper.isPresent()) {
                    uploadJVM(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
                }
            } catch (Exception e4) {
                uploadJVM(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
            }
        } else {
            uploadJVM(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
        }
        launcher.setJavaDir(new File(OS.appendBootstrapperJvm1(javaPath.toString())));
    }

    private void uploadJVM(MinecraftLauncher launcher, Path javaPath, Path javaSHA1Path, Path javaVersionPath, JavaVersionDescription javaVersionDescription) throws MinecraftException {
        if (Files.exists(javaPath.getParent(), new LinkOption[0])) {
            FileUtil.deleteDirectory(javaPath.getParent().toFile());
        }
        if (Objects.nonNull(javaVersionDescription)) {
            configureDownloadContainer(launcher, javaPath, javaSHA1Path, javaVersionPath, javaVersionDescription);
        } else if (Files.notExists(javaSHA1Path, new LinkOption[0])) {
            throw new MinecraftException("jvm folder are not valid, update files", LoginForm.DOWNLOADER_BLOCK, new Object[0]);
        }
    }

    private void configureDownloadContainer(MinecraftLauncher launcher, Path javaPath, Path javaSHA1Path, Path javaVersionPath, JavaVersionDescription javaVersionDescription) throws MinecraftException {
        if (Objects.isNull(javaVersionDescription)) {
            throw new MinecraftException("jvm folder are not valid, update files", LoginForm.DOWNLOADER_BLOCK, new Object[0]);
        }
        try {
            JVMManifest jvmManifest = (JVMManifest) this.gson.fromJson(ClientInstanceRepo.EMPTY_REPO.getUrl(javaVersionDescription.getManifest().getUrl()), (Class<Object>) JVMManifest.class);
            DownloadableContainer container = new DownloadableContainer();
            jvmManifest.getFiles().entrySet().stream().filter(e -> {
                return ((JVMFile) e.getValue()).getType().equals(Action.FILE_ATTRIBUTE);
            }).forEach(e2 -> {
                MetadataDTO m = ((JVMFile) e2.getValue()).getDownloads().getRaw();
                File destination = new File(javaPath.toString(), (String) e2.getKey());
                if (!Objects.equals(FileUtil.getChecksum(destination), m.getSha1()) || destination.length() != m.getSize()) {
                    m.setLocalDestination(destination);
                    JVMFile jvmFile = (JVMFile) e2.getValue();
                    jvmFile.setTargetPath((String) e2.getKey());
                    JVMFileDownloadable jvm = new JVMFileDownloadable(jvmFile);
                    container.add(jvm);
                }
            });
            container.addHandler(new DownloadJVMFilesHandler(javaPath, jvmManifest, javaSHA1Path, javaVersionPath, javaVersionDescription.getVersion().getName()));
            launcher.getDownloader().add(container);
        } catch (IOException e3) {
            throw new MinecraftException("jvm folder are not valid, update files", LoginForm.DOWNLOADER_BLOCK, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/minecraft/JavaMinecraftComponent$DownloadJVMFilesHandler.class */
    public class DownloadJVMFilesHandler extends DownloadableContainerHandlerAdapter {
        private Path javaPath;
        private JVMManifest jvmManifest;
        private Path javaSHA1Path;
        private Path javaVersionPath;
        private String javaVersionName1;

        public DownloadJVMFilesHandler(Path javaPath, JVMManifest jvmManifest, Path javaSHA1Path, Path javaVersionPath, String javaVersionName1) {
            this.javaPath = javaPath;
            this.jvmManifest = jvmManifest;
            this.javaSHA1Path = javaSHA1Path;
            this.javaVersionPath = javaVersionPath;
            this.javaVersionName1 = javaVersionName1;
        }

        @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
        public void onFullComplete(DownloadableContainer c) {
            if (c.getList().size() != 0) {
                try {
                    List<String> list = (List) Files.walk(this.javaPath, new FileVisitOption[0]).filter(x$0 -> {
                        return Files.isRegularFile(x$0, new LinkOption[0]);
                    }).map(e -> {
                        return String.format("%s %s %s %s", this.javaPath.relativize(e).toString(), "/#//", FileUtil.getChecksum(e.toFile()), System.currentTimeMillis() + "0000");
                    }).collect(Collectors.toList());
                    Files.write(this.javaSHA1Path, list, StandardOpenOption.CREATE);
                    Files.write(this.javaVersionPath, this.javaVersionName1.getBytes(), StandardOpenOption.CREATE);
                    this.jvmManifest.getFiles().entrySet().stream().filter(e2 -> {
                        return ((JVMFile) e2.getValue()).getType().equals("directory");
                    }).forEach(e3 -> {
                        try {
                            Files.createDirectories(Paths.get(this.javaPath.toString(), (String) e3.getKey()), new FileAttribute[0]);
                        } catch (IOException e1) {
                            U.log(e1);
                        }
                    });
                    this.jvmManifest.getFiles().entrySet().stream().filter(e4 -> {
                        return ((JVMFile) e4.getValue()).getType().equals("link");
                    }).forEach(e5 -> {
                        Path link = Paths.get(this.javaPath.toString(), (String) e5.getKey());
                        if (Files.notExists(link, new LinkOption[0])) {
                            try {
                                U.log(String.format("%s %s", Paths.get(((JVMFile) e5.getValue()).getTarget(), new String[0]), Paths.get(this.javaPath.toString(), (String) e5.getKey())));
                                Files.createSymbolicLink(link, Paths.get(((JVMFile) e5.getValue()).getTarget(), new String[0]), new FileAttribute[0]);
                            } catch (IOException e1) {
                                U.log(e1);
                            }
                        }
                    });
                } catch (IOException e6) {
                    U.log(e6);
                }
            }
        }

        @Override // org.tlauncher.tlauncher.downloader.DownloadableContainerHandlerAdapter, org.tlauncher.tlauncher.downloader.DownloadableContainerHandler
        public void onComplete(DownloadableContainer c, Downloadable d) throws RetryDownloadException {
            JVMFileDownloadable d1 = (JVMFileDownloadable) d;
            if (d1.getJvmFile().isExecutable() && !OS.is(OS.WINDOWS)) {
                try {
                    Files.setPosixFilePermissions(d.getMetadataDTO().getLocalDestination().toPath(), FileUtil.PERMISSIONS);
                } catch (IOException e) {
                    U.log(e);
                }
            }
        }
    }
}
