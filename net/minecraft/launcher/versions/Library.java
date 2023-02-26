package net.minecraft.launcher.versions;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.launcher.versions.Rule;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.downloader.Downloadable;
import org.tlauncher.tlauncher.downloader.RetryDownloadException;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tukaani.xz.XZInputStream;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Library.class */
public class Library {
    private static final String FORGE_LIB_SUFFIX = ".pack.xz";
    private static final StrSubstitutor SUBSTITUTOR;
    private String name;
    private List<Rule> rules;
    private Map<OS, String> natives;
    private ExtractRules extract;
    private List<String> deleteEntries;
    private MetadataDTO artifact;
    private Map<String, MetadataDTO> classifies;
    private String url;
    private String exact_url;
    private String packed;
    private String checksum;

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Library$TYPE.class */
    public enum TYPE {
        CLASSIFIES,
        ARTIFACT
    }

    static {
        HashMap<String, String> map = new HashMap<>();
        map.put("platform", OS.CURRENT.getName());
        map.put("arch", OS.Arch.CURRENT.asString());
        SUBSTITUTOR = new StrSubstitutor(map);
    }

    private String getArtifactBaseDir() {
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
        }
        String[] parts = this.name.split(":");
        return String.format("%s/%s/%s", parts[0].replaceAll("\\.", "/"), parts[1], parts[2]);
    }

    public String getName() {
        return this.name;
    }

    public String getPlainName() {
        String[] split = this.name.split(":");
        return split[0] + "." + split[1];
    }

    public List<Rule> getRules() {
        if (this.rules == null) {
            return null;
        }
        return Collections.unmodifiableList(this.rules);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean appliesToCurrentEnvironment() {
        if (this.rules == null) {
            return true;
        }
        Rule.Action lastAction = Rule.Action.DISALLOW;
        for (Rule rule : this.rules) {
            Rule.Action action = rule.getAppliedAction();
            if (action != null) {
                lastAction = action;
            }
        }
        return lastAction == Rule.Action.ALLOW;
    }

    public Map<OS, String> getNatives() {
        return this.natives;
    }

    public ExtractRules getExtractRules() {
        return this.extract;
    }

    public String getChecksum() {
        MetadataDTO meta = defineMetadataLibrary(OS.CURRENT);
        if (meta != null) {
            return meta.getSha1();
        }
        if (this.artifact != null) {
            return this.artifact.getSha1();
        }
        return this.checksum;
    }

    public List<String> getDeleteEntriesList() {
        return this.deleteEntries;
    }

    public String getArtifactPath(String classifier) {
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
        }
        return String.format("%s/%s", getArtifactBaseDir(), getArtifactFilename(classifier));
    }

    public String getArtifactPath() {
        return getArtifactPath(null);
    }

    private String getArtifactFilename(String classifier) {
        String result;
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
        }
        String[] parts = this.name.split(":");
        if (classifier == null) {
            result = ((String) IntStream.range(1, parts.length).mapToObj(i -> {
                return parts[i];
            }).collect(Collectors.joining("-"))) + ".jar";
        } else {
            result = String.format("%s-%s%s.jar", parts[1], parts[2], "-" + classifier);
        }
        return SUBSTITUTOR.replace(result);
    }

    public String toString() {
        return "Library{name='" + this.name + "', rules=" + this.rules + ", natives=" + this.natives + ", extract=" + this.extract + ", packed='" + this.packed + "'}";
    }

    public Downloadable getDownloadable(Repo versionSource, File file, OS os) {
        String path;
        Repo repo = null;
        boolean isForge = "forge".equals(this.packed);
        Downloadable lib = determineNewSource(file, os);
        if (lib != null) {
            return lib;
        }
        if (this.exact_url == null) {
            String nativePath = (this.natives == null || !appliesToCurrentEnvironment()) ? null : this.natives.get(os);
            path = getArtifactPath(nativePath) + (isForge ? FORGE_LIB_SUFFIX : CoreConstants.EMPTY_STRING);
            if (this.url == null) {
                repo = ClientInstanceRepo.LIBRARY_REPO;
            } else if (this.url.startsWith("/")) {
                repo = versionSource;
                path = this.url.substring(1) + path;
            } else {
                path = this.url + path;
            }
        } else {
            path = this.exact_url;
        }
        if (isForge) {
            File tempFile = new File(file.getAbsolutePath() + FORGE_LIB_SUFFIX);
            MetadataDTO metadataDTO = new MetadataDTO();
            metadataDTO.setUrl(path);
            metadataDTO.setLocalDestination(tempFile);
            return new ForgeLibDownloadable(ClientInstanceRepo.EMPTY_REPO, metadataDTO, file);
        }
        MetadataDTO metadataDTO2 = new MetadataDTO();
        metadataDTO2.setUrl(path);
        metadataDTO2.setLocalDestination(file);
        return repo == null ? new Downloadable(ClientInstanceRepo.EMPTY_REPO, metadataDTO2) : new Downloadable(repo, metadataDTO2);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Downloadable determineNewSource(File file, OS os) {
        MetadataDTO meta = defineMetadataLibrary(os);
        if (meta != null) {
            meta.setLocalDestination(file);
            return new Downloadable(ClientInstanceRepo.EMPTY_REPO, meta);
        } else if (this.artifact != null) {
            this.artifact.setLocalDestination(file);
            return new Downloadable(ClientInstanceRepo.EMPTY_REPO, this.artifact);
        } else {
            return null;
        }
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Library$LibrarySerializer.class */
    public static class LibrarySerializer implements JsonSerializer<Library>, JsonDeserializer<Library> {
        private final Gson gson;

        public LibrarySerializer() {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
            builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
            builder.enableComplexMapKeySerialization();
            this.gson = builder.create();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public Library deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = elem.getAsJsonObject();
            JsonObject downloads = object.getAsJsonObject("downloads");
            if (downloads != null) {
                JsonElement artifact = downloads.get("artifact");
                if (artifact != null) {
                    object.add("artifact", artifact);
                }
                JsonObject classifies = downloads.getAsJsonObject("classifiers");
                if (classifies != null) {
                    Set<Map.Entry<String, JsonElement>> set = classifies.entrySet();
                    JsonObject ob = new JsonObject();
                    for (Map.Entry<String, JsonElement> el : set) {
                        String key = el.getKey();
                        boolean z = true;
                        switch (key.hashCode()) {
                            case -2021876808:
                                if (key.equals("sources")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case -1819865130:
                                if (key.equals("javadoc")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case -1665975054:
                                if (key.equals("natives-windows")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case -644971232:
                                if (key.equals("linux-x86_64")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 110251553:
                                if (key.equals("tests")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 978892927:
                                if (key.equals("linux-aarch_64")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 1579283738:
                                if (key.equals("natives-windows-32")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 1579283833:
                                if (key.equals("natives-windows-64")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 1595093551:
                                if (key.equals("natives-macos-arm64")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 1767495939:
                                if (key.equals("natives-osx")) {
                                    z = true;
                                    break;
                                }
                                break;
                            case 2048441123:
                                if (key.equals("natives-linux")) {
                                    z = false;
                                    break;
                                }
                                break;
                            case 2049115554:
                                if (key.equals("natives-macos")) {
                                    z = true;
                                    break;
                                }
                                break;
                        }
                        switch (z) {
                            case false:
                                ob.add(OS.LINUX.name().toLowerCase(), el.getValue());
                                break;
                            case true:
                                ob.add(OS.WINDOWS.name().toLowerCase(), el.getValue());
                                break;
                            case true:
                                ob.add(OS.OSX.name().toLowerCase(), el.getValue());
                                break;
                            case true:
                                ob.add(OS.WINDOWS.name().toLowerCase() + "-32", el.getValue());
                                break;
                            case true:
                                ob.add(OS.WINDOWS.name().toLowerCase() + "-64", el.getValue());
                                break;
                            case true:
                                ob.add(OS.OSX.name().toLowerCase(), el.getValue());
                                break;
                            case true:
                                ob.add("tests", el.getValue());
                                break;
                            case true:
                                ob.add("sources", el.getValue());
                                break;
                            case true:
                                ob.add("javadoc", el.getValue());
                                break;
                            case true:
                                ob.add("linux-aarch_64", el.getValue());
                                break;
                            case true:
                                ob.add("linux-x86_64", el.getValue());
                                break;
                            case true:
                                ob.add("natives-macos-arm64", el.getValue());
                                break;
                            default:
                                U.log("can't find proper config for ", el);
                                break;
                        }
                    }
                    object.add("classifies", ob);
                }
                object.remove("downloads");
            }
            return (Library) this.gson.fromJson(elem, (Class<Object>) Library.class);
        }

        @Override // com.google.gson.JsonSerializer
        public JsonElement serialize(Library library, Type type, JsonSerializationContext context) {
            return this.gson.toJsonTree(library, type);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static synchronized void unpackLibrary(File library, File output, boolean retryOnOutOfMemory) throws IOException {
        forgeLibLog("Synchronized unpacking:", library);
        output.delete();
        try {
            try {
                InputStream in = new FileInputStream(library);
                XZInputStream xZInputStream = new XZInputStream(in);
                forgeLibLog("Decompressing...");
                byte[] decompressed = readFully(xZInputStream);
                forgeLibLog("Decompressed successfully");
                String end = new String(decompressed, decompressed.length - 4, 4);
                if (!end.equals("SIGN")) {
                    throw new RetryDownloadException("signature missing");
                }
                forgeLibLog("Signature matches!");
                int x = decompressed.length;
                int len = (decompressed[x - 8] & 255) | ((decompressed[x - 7] & 255) << 8) | ((decompressed[x - 6] & 255) << 16) | ((decompressed[x - 5] & 255) << 24);
                forgeLibLog("Now getting checksums...");
                byte[] checksums = Arrays.copyOfRange(decompressed, (decompressed.length - len) - 8, decompressed.length - 8);
                FileUtil.createFile(output);
                FileOutputStream jarBytes = new FileOutputStream(output);
                JarOutputStream jos = new JarOutputStream(jarBytes);
                forgeLibLog("Now unpacking...");
                Pack200.newUnpacker().unpack(new ByteArrayInputStream(decompressed), jos);
                forgeLibLog("Unpacked successfully");
                forgeLibLog("Now trying to write checksums...");
                jos.putNextEntry(new JarEntry("checksums.sha1"));
                jos.write(checksums);
                jos.closeEntry();
                forgeLibLog("Now finishing...");
                close(xZInputStream, jos);
                FileUtil.deleteFile(library);
                forgeLibLog("Done:", output);
            } catch (IOException e) {
                output.delete();
                throw e;
            } catch (OutOfMemoryError oomE) {
                forgeLibLog("Out of memory, oops", oomE);
                U.gc();
                if (retryOnOutOfMemory) {
                    forgeLibLog("Retrying...");
                    close(null, null);
                    FileUtil.deleteFile(library);
                    unpackLibrary(library, output, false);
                    close(null, null);
                    FileUtil.deleteFile(library);
                    return;
                }
                throw oomE;
            }
        } catch (Throwable th) {
            close(null, null);
            FileUtil.deleteFile(library);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void unpackLibrary(File library, File output) throws IOException {
        unpackLibrary(library, output, true);
    }

    private static void close(Closeable... closeables) {
        for (Closeable c : closeables) {
            try {
                c.close();
            } catch (Exception e) {
            }
        }
    }

    private static byte[] readFully(InputStream stream) throws IOException {
        int len;
        byte[] data = new byte[CpioConstants.C_ISFIFO];
        ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
        do {
            len = stream.read(data);
            if (len > 0) {
                entryBuffer.write(data, 0, len);
            }
        } while (len != -1);
        return entryBuffer.toByteArray();
    }

    private static void forgeLibLog(Object... o) {
        U.log("[ForgeLibDownloadable]", o);
    }

    private MetadataDTO defineMetadataLibrary(OS os) {
        if (this.classifies != null && this.natives != null) {
            if (this.classifies.get(os.name().toLowerCase()) != null) {
                return this.classifies.get(os.name().toLowerCase());
            }
            if (this.classifies.get(os.name().toLowerCase() + "-" + OS.Arch.CURRENT.name().replace("x", CoreConstants.EMPTY_STRING)) != null) {
                return this.classifies.get(os.name().toLowerCase() + "-" + OS.Arch.CURRENT.name().replace("x", CoreConstants.EMPTY_STRING));
            }
            return null;
        }
        return null;
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/Library$ForgeLibDownloadable.class */
    public static class ForgeLibDownloadable extends Downloadable {
        private final File unpacked;

        private ForgeLibDownloadable(Repo rep, MetadataDTO metadataDTO, File unpackedLib) {
            super(rep, metadataDTO);
            this.unpacked = unpackedLib;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.tlauncher.tlauncher.downloader.Downloadable
        public void onComplete() throws RetryDownloadException {
            super.onComplete();
            try {
                Library.unpackLibrary(getMetadataDTO().getLocalDestination(), this.unpacked);
            } catch (Throwable t) {
                throw new RetryDownloadException("cannot unpack forge library", t);
            }
        }
    }
}
