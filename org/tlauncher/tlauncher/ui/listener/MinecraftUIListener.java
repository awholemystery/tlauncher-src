package org.tlauncher.tlauncher.ui.listener;

import ch.qos.logback.core.CoreConstants;
import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.launcher.Http;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.json.Argument;
import net.minecraft.launcher.versions.json.ArgumentType;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpStatus;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.test.environment.TestEnvironment;
import org.tlauncher.tlauncher.minecraft.crash.Crash;
import org.tlauncher.tlauncher.minecraft.crash.CrashSignatureContainer;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftException;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener;
import org.tlauncher.tlauncher.minecraft.launcher.assitent.LanguageAssistance;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.tlauncher.ui.settings.MinecraftSettings;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.StringUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/listener/MinecraftUIListener.class */
public class MinecraftUIListener implements MinecraftListener {
    private final TLauncher t;
    private final LangConfiguration lang;

    public MinecraftUIListener(TLauncher tlauncher) {
        this.t = tlauncher;
        this.lang = this.t.getLang();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftPrepare() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftAbort() {
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        if (!this.t.getConfiguration().getActionOnLaunch().equals(ActionOnLaunch.NOTHING)) {
            this.t.hide();
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftClose() {
        if (!this.t.getLauncher().isLaunchAssist()) {
            return;
        }
        this.t.show();
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftCrash(Crash crash) {
        if (!this.t.getLauncher().isLaunchAssist()) {
            this.t.show();
        }
        Configuration c = this.t.getConfiguration();
        String title = Localizable.get("crash.title");
        String report = crash.getFile();
        if (!crash.isRecognized()) {
            showPossibleSolutions();
            return;
        }
        for (CrashSignatureContainer.CrashSignature sign : crash.getSignatures()) {
            String path = sign.getPath();
            String message = "crash." + path;
            if ("not proper gpu1".equalsIgnoreCase(sign.getName()) && !c.getBoolean("fixed.gpu.jre.error") && OS.is(OS.WINDOWS)) {
                c.set("fixed.gpu.jre.error", (Object) true);
                U.log("set new jre");
            } else if ("Bad video drivers".equalsIgnoreCase(sign.getName()) && !c.getBoolean("fixed.gpu.jre.error") && OS.is(OS.WINDOWS) && OS.VERSION.contains("10") && (StringUtils.contains(this.t.getConfiguration().get("gpu.info.full"), "Intel(R) HD Graphics Family") || StringUtils.contains(this.t.getConfiguration().get("gpu.info.full"), "Pentium"))) {
                c.set("fixed.gpu.jre.error", (Object) true);
                Alert.showLocMessageWithoutTitle("crash.restart");
                U.log("set new jre");
            } else if (c.getBoolean("fixed.gpu.jre.error") && "Bad video drivers".equalsIgnoreCase(sign.getName())) {
                Alert.showLocMessageWithoutTitle("crash.opengl.windows10.error");
            }
            if (sign.getName().equalsIgnoreCase("thread creation problem")) {
                if (!replaceJVMParam()) {
                    showPossibleSolutions();
                    return;
                }
            } else if (sign.getName().equalsIgnoreCase("Not reserve ram") && this.t.getConfiguration().getInteger(MinecraftSettings.MINECRAFT_SETTING_RAM) == OS.Arch.PREFERRED_MEMORY) {
                showProperMemoryMessage();
            } else if (sign.getName().equalsIgnoreCase("Not reserve ram") || sign.getName().equalsIgnoreCase("jetty ram problem")) {
                if (Alert.showLocQuestion(title, message)) {
                    this.t.getConfiguration().set(MinecraftSettings.MINECRAFT_SETTING_RAM, Integer.valueOf(OS.Arch.PREFERRED_MEMORY), true);
                    return;
                }
                return;
            } else if (sign.getName().equalsIgnoreCase("gson lenient") && !this.t.getConfiguration().getBoolean("skip.account.property")) {
                this.t.getConfiguration().set("skip.account.property", (Object) true);
            } else if (sign.getName().equalsIgnoreCase("Bad video drivers")) {
                String gpuLink = CoreConstants.EMPTY_STRING;
                String gpuName = this.t.getConfiguration().get("gpu.info");
                String cpuLink = CoreConstants.EMPTY_STRING;
                String cpuName = this.t.getConfiguration().get("process.info");
                int i = 2;
                if (!Strings.isNullOrEmpty(gpuName)) {
                    String gpuLink2 = Http.get("https://www.google.com/search", "q", String.join(" ", gpuName, OS.NAME, OS.VERSION, Localizable.get("crash.opengl.download.driver")));
                    i = 2 + 1;
                    gpuLink = String.format("<br>%s)%s <a href='%s'>%s</a>", 2, Localizable.get("crash.opengl.install.gpu"), gpuLink2, Localizable.get("click.me"));
                }
                if (!Strings.isNullOrEmpty(cpuName)) {
                    String cpuLink2 = Http.get("https://www.google.com/search", "q", String.join(" ", cpuName, OS.NAME, OS.VERSION, Localizable.get("crash.opengl.download.driver")));
                    int i2 = i;
                    int i3 = i + 1;
                    cpuLink = String.format("<br>%s)%s <a href='%s'>%s</a>", Integer.valueOf(i2), Localizable.get("crash.opengl.install.cpu"), cpuLink2, Localizable.get("click.me"));
                }
                Alert.showErrorHtml(CoreConstants.EMPTY_STRING, String.format(Localizable.get(message), gpuLink, cpuLink, Localizable.get("crash.opengl.help")));
            } else if ("vbo fix".equals(sign.getName()) && !fixProblem()) {
                showPossibleSolutions();
                return;
            } else if (("OutOfMemory error".equals(sign.getName()) || "PermGen error".equals(sign.getName())) && "system.32x.not.proper".equals(c.get(TestEnvironment.WARMING_MESSAGE))) {
                message = Localizable.get(message) + Localizable.get("crash.outofmemory.32bit");
            } else if ("problem g1gc".equals(sign.getName())) {
                Alert.showLocMessageWithoutTitle(message);
                Alert.showLocMessageWithoutTitle("crash.swap.file.increase");
                this.t.getConfiguration().set(MinecraftSettings.MINECRAFT_SETTING_RAM, Integer.valueOf(OS.Arch.PREFERRED_MEMORY), true);
                return;
            } else if ("failed create event loop".equals(sign.getName())) {
                long l = c.getLong("reset.net");
                if (l == 0 || new Date(l).before(new Date())) {
                    executeFixed();
                    return;
                }
            } else if ("forge config error".equalsIgnoreCase(sign.getName())) {
                Path configDir = Paths.get(this.t.getLauncher().getRunningMinecraftDir().getPath(), "config");
                if (Files.exists(configDir, new LinkOption[0]) && Files.isDirectory(configDir, new LinkOption[0])) {
                    FileUtil.deleteDirectory(configDir.toFile());
                }
            }
            Alert.showLocMessage(title, message, report);
        }
    }

    private void executeFixed() {
        Alert.showLocMessage(CoreConstants.EMPTY_STRING, "crash.switch.antivirus.system.auto", null);
        try {
            Path runner = Files.createTempFile("TLauncherUpdater", ".exe", new FileAttribute[0]);
            FileUtils.copyURLToFile(new URL("http://repo.tlauncher.org/update/downloads/libraries/org/tlauncher/updater/TLauncherUpdater.exe"), runner.toFile(), 30000, 30000);
            Process p = Runtime.getRuntime().exec(new String[]{"cmd", "/c", runner.toString(), "\\\"netsh winsock reset\\\""});
            int code = p.waitFor();
            if (code == 0) {
                this.t.getConfiguration().set("reset.net", Long.valueOf(DateUtils.addDays(new Date(), 7).getTime()), true);
            }
            U.log("finished fixed with code " + code);
        } catch (IOException | InterruptedException e) {
            U.log(e);
        }
    }

    private boolean fixProblem() {
        boolean canFixed = false;
        File options = new File(this.t.getLauncher().getRunningMinecraftDir(), LanguageAssistance.OPTIONS);
        if (options.exists()) {
            try {
                List<String> lines = Files.readAllLines(options.toPath(), StandardCharsets.UTF_8);
                int indexVsync = -1;
                int indexVbo = -1;
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains("enableVsync:")) {
                        indexVsync = i;
                    }
                    if (lines.get(i).contains("useVbo:")) {
                        indexVbo = i;
                    }
                }
                if (indexVbo == -1) {
                    lines.add("useVbo:false");
                    canFixed = true;
                } else if (lines.get(indexVbo).endsWith("true")) {
                    lines.remove(indexVbo);
                    lines.add(indexVbo, "useVbo:false");
                    canFixed = true;
                }
                if (indexVsync == -1) {
                    lines.add("enableVsync:true");
                    canFixed = true;
                } else if (lines.get(indexVsync).endsWith("false")) {
                    lines.remove(indexVsync);
                    lines.add(indexVsync, "enableVsync:true");
                    canFixed = true;
                }
                if (canFixed) {
                    FileUtil.writeFile(options, String.join(System.lineSeparator(), lines));
                }
            } catch (Throwable t) {
                U.log(t);
                return false;
            }
        }
        return canFixed;
    }

    private void showProperMemoryMessage() {
        String link;
        String recommendedMemory = CoreConstants.EMPTY_STRING;
        if (TLauncher.getInstance().getConfiguration().isUSSRLocale()) {
            link = StringUtil.getLink("https://www.dmosk.ru/polezno.php?review=memory-notfull");
        } else {
            link = StringUtil.getLink("https://www.howtogeek.com/131632/hardware-upgrade-why-windows-cant-see-all-your-ram") + "<br>" + StringUtil.getLink("https://windowsreport.com/windows-10-isnt-using-all-ram");
        }
        if (OS.Arch.TOTAL_RAM_MB < 1600) {
            recommendedMemory = Localizable.get("crash.java.not.enough.memory.solve.additional.low.memory");
        }
        Alert.showErrorHtml(String.format(Localizable.get("crash.java.not.enough.memory.solve"), Integer.valueOf(OS.Arch.TOTAL_RAM_MB), recommendedMemory, link), 500);
    }

    private boolean replaceJVMParam() {
        try {
            CompleteVersion version = this.t.getVersionManager().getLocalList().getCompleteVersion(this.t.getLauncher().getVersion().getID());
            Map<ArgumentType, List<Argument>> args = version.getArguments();
            if (Objects.nonNull(args) && Objects.nonNull(args.get(ArgumentType.JVM))) {
                List<Argument> list = args.get(ArgumentType.JVM);
                for (int i = 0; i < list.size(); i++) {
                    Optional<String> xss1024 = Arrays.stream(list.get(i).getValues()).filter(e -> {
                        return e.equalsIgnoreCase("-Xss1M");
                    }).findAny();
                    Optional<String> xss512 = Arrays.stream(list.get(i).getValues()).filter(e2 -> {
                        return e2.equalsIgnoreCase("-Xss512K");
                    }).findAny();
                    if (xss1024.isPresent()) {
                        int ram = OS.Arch.PREFERRED_MEMORY - 256;
                        if (ram > 512) {
                            this.t.getConfiguration().set(MinecraftSettings.MINECRAFT_SETTING_RAM, Integer.valueOf(ram), false);
                        }
                        list.remove(i);
                        list.add(i, new Argument(new String[]{"-Xss512K"}, null));
                        return true;
                    } else if (xss512.isPresent()) {
                        list.remove(i);
                        list.add(i, new Argument(new String[]{"-Xss256K"}, null));
                        return true;
                    }
                }
                this.t.getVersionManager().getLocalList().saveVersion(version);
            }
            return false;
        } catch (IOException e3) {
            U.log(new Object[0]);
            return false;
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftError(Throwable e) {
        showPossibleSolutions();
    }

    private void showPossibleSolutions() {
        MinecraftLauncher launcher = this.t.getLauncher();
        if (Objects.nonNull(launcher.getVersion()) && launcher.getVersion().isModpack()) {
            Alert.showErrorHtml("launcher.error.modpack.solutions", (int) HttpStatus.SC_BAD_REQUEST);
            return;
        }
        File mods = new File(MinecraftUtil.getWorkingDirectory(), "mods");
        File resourcepacks = new File(MinecraftUtil.getWorkingDirectory(), "resourcepacks");
        File shaderpacks = new File(MinecraftUtil.getWorkingDirectory(), "shaderpacks");
        String modpackStructureMessage = CoreConstants.EMPTY_STRING;
        if (mods.exists() && FileUtils.listFiles(mods, new String[]{ArchiveStreamFactory.JAR, ArchiveStreamFactory.ZIP}, true).size() > 2) {
            modpackStructureMessage = Localizable.get("launcher.error.standard.version.point.mod");
        } else if (resourcepacks.exists() && !FileUtils.listFiles(resourcepacks, new String[]{ArchiveStreamFactory.ZIP}, true).isEmpty()) {
            modpackStructureMessage = Localizable.get("launcher.error.standard.version.point.resourcepack");
        } else if (shaderpacks.exists() && !FileUtils.listFiles(shaderpacks, new String[]{ArchiveStreamFactory.ZIP}, true).isEmpty()) {
            modpackStructureMessage = Localizable.get("launcher.error.standard.version.point.shaderpack");
        }
        if (!modpackStructureMessage.isEmpty()) {
            Alert.showErrorHtml(Localizable.get(modpackStructureMessage, Localizable.get("crash.opengl.help")), 600);
        } else {
            Alert.showErrorHtml(Localizable.get("launcher.error.standard.version.solutions", modpackStructureMessage, Localizable.get("crash.opengl.help")), (int) HttpStatus.SC_BAD_REQUEST);
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftKnownError(MinecraftException e) {
        Alert.showError(this.lang.get("launcher.error.title"), this.lang.get("launcher.error." + e.getLangPath(), e.getLangVars()), e.getCause());
    }
}
