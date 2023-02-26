package net.minecraft.launcher.versions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.launcher.updater.AssetIndex;
import net.minecraft.launcher.updater.VersionList;
import net.minecraft.launcher.updater.VersionSyncInfo;
import net.minecraft.launcher.versions.Library;
import net.minecraft.launcher.versions.Rule;
import net.minecraft.launcher.versions.json.Argument;
import net.minecraft.launcher.versions.json.ArgumentType;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
import net.minecraft.launcher.versions.json.RepoTypeAdapter;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.version.MetadataDTO;
import org.tlauncher.tlauncher.entity.PathAppUtil;
import org.tlauncher.tlauncher.entity.TLauncherLib;
import org.tlauncher.tlauncher.managers.VersionManager;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.OS;
import org.tlauncher.util.U;
import org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/CompleteVersion.class */
public class CompleteVersion implements Cloneable, Version {
    String id;
    String inheritsFrom;
    Date time;
    Date releaseTime;
    ReleaseType type;
    String jvmArguments;
    String minecraftArguments;
    String mainClass;
    Map<ArgumentType, List<Argument>> arguments;
    String assets;
    VersionList list;
    List<Library> libraries;
    List<Rule> rules;
    List<String> deleteEntries;
    private AssetsMetadata assetIndex;
    Map<String, MetadataDTO> downloads;
    private Double complianceLevel;
    private JavaVersionName javaVersion;
    private Map<String, LogClient> logging;
    Integer minimumLauncherVersion = 0;
    transient ModifiedVersion modifiedVersion = new ModifiedVersion();

    public Map<String, LogClient> getLogging() {
        return this.logging;
    }

    public void setLogging(Map<String, LogClient> logging) {
        this.logging = logging;
    }

    public void setModifiedVersion(ModifiedVersion modifiedVersion) {
        this.modifiedVersion = modifiedVersion;
    }

    public String getRemoteVersion() {
        return this.modifiedVersion.getRemoteVersion();
    }

    public ModifiedVersion getModifiedVersion() {
        return this.modifiedVersion;
    }

    public boolean isModpack() {
        return this.modifiedVersion.getModpack() != null;
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/CompleteVersion$AssetsMetadata.class */
    public static class AssetsMetadata {
        private String id;
        private int totalSize;
        protected String name;
        protected long size;
        protected String path;
        protected String url;

        public void setId(String id) {
            this.id = id;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof AssetsMetadata) {
                AssetsMetadata other = (AssetsMetadata) o;
                if (other.canEqual(this)) {
                    Object this$id = getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id != null) {
                            return false;
                        }
                    } else if (!this$id.equals(other$id)) {
                        return false;
                    }
                    if (getTotalSize() != other.getTotalSize()) {
                        return false;
                    }
                    Object this$name = getName();
                    Object other$name = other.getName();
                    if (this$name == null) {
                        if (other$name != null) {
                            return false;
                        }
                    } else if (!this$name.equals(other$name)) {
                        return false;
                    }
                    if (getSize() != other.getSize()) {
                        return false;
                    }
                    Object this$path = getPath();
                    Object other$path = other.getPath();
                    if (this$path == null) {
                        if (other$path != null) {
                            return false;
                        }
                    } else if (!this$path.equals(other$path)) {
                        return false;
                    }
                    Object this$url = getUrl();
                    Object other$url = other.getUrl();
                    return this$url == null ? other$url == null : this$url.equals(other$url);
                }
                return false;
            }
            return false;
        }

        protected boolean canEqual(Object other) {
            return other instanceof AssetsMetadata;
        }

        public int hashCode() {
            Object $id = getId();
            int result = (1 * 59) + ($id == null ? 43 : $id.hashCode());
            int result2 = (result * 59) + getTotalSize();
            Object $name = getName();
            int result3 = (result2 * 59) + ($name == null ? 43 : $name.hashCode());
            long $size = getSize();
            int result4 = (result3 * 59) + ((int) (($size >>> 32) ^ $size));
            Object $path = getPath();
            int result5 = (result4 * 59) + ($path == null ? 43 : $path.hashCode());
            Object $url = getUrl();
            return (result5 * 59) + ($url == null ? 43 : $url.hashCode());
        }

        public String toString() {
            return "CompleteVersion.AssetsMetadata(id=" + getId() + ", totalSize=" + getTotalSize() + ", name=" + getName() + ", size=" + getSize() + ", path=" + getPath() + ", url=" + getUrl() + ")";
        }

        public String getId() {
            return this.id;
        }

        public int getTotalSize() {
            return this.totalSize;
        }

        public String getName() {
            return this.name;
        }

        public long getSize() {
            return this.size;
        }

        public String getPath() {
            return this.path;
        }

        public String getUrl() {
            return this.url;
        }
    }

    public AssetsMetadata getAssetIndex() {
        return this.assetIndex;
    }

    public void setAssetIndex(AssetsMetadata assetIndex) {
        this.assetIndex = assetIndex;
    }

    public Map<String, MetadataDTO> getDownloads() {
        return this.downloads;
    }

    public void setDownloads(Map<String, MetadataDTO> downloads) {
        this.downloads = downloads;
    }

    public List<Library> getModsLibraries() {
        return this.modifiedVersion.getModsLibraries();
    }

    public void setModsLibraries(List<Library> modsLibraries) {
        this.modifiedVersion.setModsLibraries(modsLibraries);
    }

    @Override // net.minecraft.launcher.versions.Version
    public String getID() {
        return this.id;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setID(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID is NULL or empty");
        }
        this.id = id;
    }

    @Override // net.minecraft.launcher.versions.Version
    public ReleaseType getReleaseType() {
        return this.type;
    }

    public void setType(ReleaseType type) {
        this.type = type;
    }

    @Override // net.minecraft.launcher.versions.Version
    public Repo getSource() {
        return this.modifiedVersion.getSource();
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setSource(Repo repo) {
        if (repo == null) {
            throw new NullPointerException();
        }
        this.modifiedVersion.setSource(repo);
    }

    @Override // net.minecraft.launcher.versions.Version
    public Date getUpdatedTime() {
        if (this.modifiedVersion.getUpdatedTime() != null) {
            return this.modifiedVersion.getUpdatedTime();
        }
        return this.time;
    }

    public void setUpdatedTime(Date time) {
        if (time == null) {
            throw new NullPointerException("Time is NULL!");
        }
        this.modifiedVersion.setUpdatedTime(time);
    }

    @Override // net.minecraft.launcher.versions.Version
    public Date getReleaseTime() {
        return this.releaseTime;
    }

    @Override // net.minecraft.launcher.versions.Version
    public VersionList getVersionList() {
        return this.list;
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setVersionList(VersionList list) {
        if (list == null) {
            throw new NullPointerException("VersionList cannot be NULL!");
        }
        this.list = list;
    }

    @Override // net.minecraft.launcher.versions.Version
    public boolean isSkinVersion() {
        return this.modifiedVersion.isSkinVersion();
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setSkinVersion(boolean skinVersion) {
        this.modifiedVersion.setSkinVersion(skinVersion);
    }

    @Override // net.minecraft.launcher.versions.Version
    public String getUrl() {
        return this.modifiedVersion.getUrl();
    }

    @Override // net.minecraft.launcher.versions.Version
    public void setUrl(String url) {
        this.modifiedVersion.setUrl(url);
    }

    @Override // net.minecraft.launcher.versions.Version
    public String getJar() {
        return this.modifiedVersion.getJar();
    }

    public String getInheritsFrom() {
        return this.inheritsFrom;
    }

    public String getJVMArguments() {
        return this.jvmArguments;
    }

    public String getMinecraftArguments() {
        return this.minecraftArguments;
    }

    public void setMinecraftArguments(String args) {
        this.minecraftArguments = args;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public void setMainClass(String clazz) {
        this.mainClass = clazz;
    }

    public List<Library> getLibraries() {
        return this.libraries;
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(this.rules);
    }

    public List<String> getDeleteEntries() {
        return this.deleteEntries;
    }

    public int getMinimumLauncherVersion() {
        return this.minimumLauncherVersion.intValue();
    }

    public int getMinimumCustomLauncherVersion() {
        return this.modifiedVersion.getTlauncherVersion().intValue();
    }

    public String getAssets() {
        return this.assets == null ? AssetIndex.DEFAULT_ASSET_NAME : this.assets;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (hashCode() == o.hashCode()) {
            return true;
        }
        if (!(o instanceof Version)) {
            return false;
        }
        Version compare = (Version) o;
        if (compare.getID() == null) {
            return false;
        }
        return compare.getID().equals(this.id);
    }

    public String toString() {
        return getClass().getSimpleName() + "{id='" + this.id + "', time=" + this.time + ", release=" + this.releaseTime + ", type=" + this.type + ", class=" + this.mainClass + ", minimumVersion=" + this.minimumLauncherVersion + ", assets='" + this.assets + "', source=" + this.modifiedVersion.getSource() + ", list=" + this.list + ", libraries=" + this.libraries + "}";
    }

    public File getFile(File base) {
        return new File(base, "versions/" + getID() + "/" + getID() + ".jar");
    }

    public boolean appliesToCurrentEnvironment() {
        if (this.rules == null) {
            return true;
        }
        for (Rule rule : this.rules) {
            Rule.Action action = rule.getAppliedAction();
            if (action == Rule.Action.DISALLOW) {
                return false;
            }
        }
        return true;
    }

    public Collection<Library> getRelevantLibraries() {
        List<Library> result = new ArrayList<>();
        for (Library library : this.libraries) {
            if (library.appliesToCurrentEnvironment()) {
                result.add(library);
            }
        }
        return result;
    }

    public Collection<File> getClassPath(OS os, File base) {
        Collection<Library> libraries = getRelevantLibraries();
        Collection<File> result = new ArrayList<>();
        for (Library library : libraries) {
            if (library.getNatives() == null) {
                result.add(new File(base, "libraries/" + library.getArtifactPath()));
            }
        }
        result.add(new File(base, "versions/" + getID() + "/" + getID() + ".jar"));
        return result;
    }

    public Collection<File> getClassPath(File base) {
        return getClassPath(OS.CURRENT, base);
    }

    public Collection<String> getNatives(OS os) {
        Collection<Library> libraries = getRelevantLibraries();
        Collection<String> result = new ArrayList<>();
        for (Library library : libraries) {
            Map<OS, String> natives = library.getNatives();
            if (natives != null && natives.containsKey(os)) {
                result.add("libraries/" + library.getArtifactPath(natives.get(os)));
            }
        }
        return result;
    }

    public Collection<String> getNatives() {
        return getNatives(OS.CURRENT);
    }

    public Set<String> getRequiredFiles(OS os) {
        Set<String> neededFiles = new HashSet<>();
        for (Library library : getRelevantLibraries()) {
            if (library.getNatives() != null) {
                String natives = library.getNatives().get(os);
                if (natives != null) {
                    neededFiles.add("libraries/" + library.getArtifactPath(natives));
                }
            } else {
                neededFiles.add("libraries/" + library.getArtifactPath());
            }
        }
        return neededFiles;
    }

    public Collection<String> getExtractFiles(OS os) {
        Collection<Library> libraries = getRelevantLibraries();
        Collection<String> result = new ArrayList<>();
        for (Library library : libraries) {
            Map<OS, String> natives = library.getNatives();
            if (natives != null && natives.containsKey(os)) {
                result.add("libraries/" + library.getArtifactPath(natives.get(os)));
            }
        }
        return result;
    }

    public CompleteVersion resolve(VersionManager vm, boolean useLatest) throws IOException {
        return resolve(vm, useLatest, new ArrayList());
    }

    protected CompleteVersion resolve(VersionManager vm, boolean useLatest, List<String> inheristance) throws IOException {
        if (vm == null) {
            throw new NullPointerException("version manager");
        }
        if (this.inheritsFrom == null) {
            return this;
        }
        log("Resolving...");
        if (inheristance.contains(this.id)) {
            throw new IllegalArgumentException(this.id + " should be already resolved.");
        }
        inheristance.add(this.id);
        log("Inherits from", this.inheritsFrom);
        VersionSyncInfo parentSyncInfo = vm.getVersionSyncInfo(this.inheritsFrom);
        if (parentSyncInfo == null) {
            return null;
        }
        try {
            CompleteVersion result = (CompleteVersion) parentSyncInfo.getCompleteVersion(useLatest).resolve(vm, useLatest, inheristance).clone();
            return partCopyInto(result);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private CompleteVersion partCopyInto(CompleteVersion result) {
        result.id = this.id;
        result.modifiedVersion = new ModifiedVersion();
        if (this.modifiedVersion.getJar() != null) {
            result.modifiedVersion.setJar(this.modifiedVersion.getJar());
        }
        result.inheritsFrom = null;
        if (this.time.getTime() != 0) {
            result.time = this.time;
        }
        result.type = this.type;
        if (this.jvmArguments != null) {
            result.jvmArguments = this.jvmArguments;
        }
        if (this.minecraftArguments != null) {
            result.minecraftArguments = this.minecraftArguments;
        }
        if (Objects.nonNull(result.arguments) || Objects.nonNull(this.arguments)) {
            Map<ArgumentType, List<Argument>> args = new HashMap<>();
            args.put(ArgumentType.GAME, new ArrayList<>());
            args.put(ArgumentType.JVM, new ArrayList<>());
            if (Objects.nonNull(result.arguments)) {
                result.arguments.forEach(key, value -> {
                    ((List) args.get(key)).addAll(value);
                });
            }
            if (Objects.nonNull(this.arguments)) {
                this.arguments.forEach(key2, value2 -> {
                    ((List) args.get(key2)).addAll(value2);
                });
            }
            result.arguments = args;
        }
        if (this.mainClass != null) {
            result.mainClass = this.mainClass;
        }
        if (this.libraries != null) {
            List<Library> newLibraries = new ArrayList<>(this.libraries);
            if (result.libraries != null) {
                newLibraries.addAll(result.libraries);
            }
            result.libraries = newLibraries;
        }
        if (this.rules != null) {
            if (result.rules != null) {
                result.rules.addAll(this.rules);
            } else {
                List<Rule> rulesCopy = new ArrayList<>(this.rules.size());
                Collections.copy(rulesCopy, this.rules);
                result.rules = this.rules;
            }
        }
        if (this.deleteEntries != null) {
            if (result.deleteEntries != null) {
                result.deleteEntries.addAll(this.deleteEntries);
            } else {
                result.deleteEntries = new ArrayList(this.deleteEntries);
            }
        }
        if (this.minimumLauncherVersion.intValue() != 0) {
            result.minimumLauncherVersion = this.minimumLauncherVersion;
        }
        if (this.assets != null && !this.assets.equals(AssetIndex.DEFAULT_ASSET_NAME)) {
            result.assets = this.assets;
        }
        result.setSkinVersion(isSkinVersion());
        if (this.assetIndex != null) {
            result.setAssetIndex(getAssetIndex());
        }
        if (getDownloads() != null) {
            result.setDownloads(getDownloads());
        }
        if (this.modifiedVersion.getModsLibraries() != null) {
            List<Library> newLibraries2 = new ArrayList<>(this.modifiedVersion.getModsLibraries());
            if (result.modifiedVersion.getModsLibraries() != null) {
                newLibraries2.addAll(result.modifiedVersion.getModsLibraries());
            }
            result.modifiedVersion.setModsLibraries(newLibraries2);
        }
        if (this.modifiedVersion.getModpack() != null) {
            ModpackDTO newModpack = new ModpackDTO();
            this.modifiedVersion.getModpack().copy(newModpack);
            result.modifiedVersion.setModpack(newModpack);
        }
        if (result.releaseTime == null) {
            result.releaseTime = this.releaseTime;
        }
        if (Objects.nonNull(this.modifiedVersion.getAdditionalFiles())) {
            result.modifiedVersion.setAdditionalFiles(new ArrayList(this.modifiedVersion.getAdditionalFiles()));
        }
        result.modifiedVersion.setActivateSkinCapeForUserVersion(this.modifiedVersion.isActivateSkinCapeForUserVersion());
        result.modifiedVersion.setRemoteVersion(getRemoteVersion());
        result.modifiedVersion.setUpdatedTime(getModifiedVersion().getUpdatedTime());
        if (Objects.nonNull(getDownloads())) {
            result.setDownloads(new HashMap(getDownloads()));
        }
        result.list = this.list;
        if (Objects.nonNull(getJavaVersion())) {
            result.setJavaVersion(getJavaVersion());
        }
        if (Objects.nonNull(getLogging()) && Objects.nonNull(getLogging().get("client"))) {
            result.setLogging(getLogging());
        }
        return result;
    }

    public CompleteVersion fullCopy(CompleteVersion c) {
        partCopyInto(c);
        c.inheritsFrom = this.inheritsFrom;
        return c;
    }

    public Map<ArgumentType, List<Argument>> getArguments() {
        return this.arguments;
    }

    public void setArguments(Map<ArgumentType, List<Argument>> arguments) {
        this.arguments = arguments;
    }

    private void log(Object... o) {
        U.log("[Version:" + this.id + "]", o);
    }

    public ModpackDTO getModpack() {
        return this.modifiedVersion.getModpack();
    }

    public void setModpackDTO(ModpackDTO modpack) {
        this.modifiedVersion.setModpack(modpack);
    }

    public List<MetadataDTO> getAdditionalFiles() {
        return this.modifiedVersion.getAdditionalFiles();
    }

    public void setAdditionalFiles(List<MetadataDTO> additionalFiles) {
        this.modifiedVersion.setAdditionalFiles(additionalFiles);
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/CompleteVersion$CompleteVersionSerializer.class */
    public static class CompleteVersionSerializer implements JsonSerializer<CompleteVersion>, JsonDeserializer<CompleteVersion> {
        private final Gson remoteContext;

        public CompleteVersionSerializer() {
            GsonBuilder remoteBuilder = new GsonBuilder();
            remoteBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
            remoteBuilder.registerTypeAdapter(Argument.class, new Argument.Serializer());
            remoteBuilder.registerTypeAdapter(Library.class, new Library.LibrarySerializer());
            remoteBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
            remoteBuilder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
            remoteBuilder.registerTypeAdapter(Repo.class, new RepoTypeAdapter());
            remoteBuilder.registerTypeAdapter(ModpackDTO.class, new ModpackDTOTypeAdapter());
            remoteBuilder.enableComplexMapKeySerialization();
            remoteBuilder.disableHtmlEscaping();
            remoteBuilder.setPrettyPrinting();
            this.remoteContext = remoteBuilder.create();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public CompleteVersion deserialize(JsonElement elem, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = elem.getAsJsonObject();
            JsonElement originalId = object.get("original_id");
            if (Objects.nonNull(object.get(TLauncherLib.USER_CONFIG_SKIN_VERSION))) {
                object.remove(TLauncherLib.USER_CONFIG_SKIN_VERSION);
            }
            if (originalId != null && originalId.isJsonPrimitive()) {
                String jar = originalId.getAsString();
                object.remove("original_id");
                object.addProperty(ArchiveStreamFactory.JAR, jar);
            }
            if (Objects.nonNull(object.get("inheritsFrom")) && Objects.isNull(object.get(ArchiveStreamFactory.JAR))) {
                object.addProperty(ArchiveStreamFactory.JAR, object.get("inheritsFrom").getAsString());
            }
            JsonElement unnecessaryEntries = object.get("unnecessaryEntries");
            if (unnecessaryEntries != null && unnecessaryEntries.isJsonArray()) {
                object.remove("unnecessaryEntries");
                object.add("deleteEntries", unnecessaryEntries);
            }
            CompleteVersion version = (CompleteVersion) this.remoteContext.fromJson((JsonElement) object, (Class<Object>) CompleteVersion.class);
            if (version.id == null) {
                throw new JsonParseException("Version ID is NULL!");
            }
            if (version.type == null) {
                version.type = ReleaseType.UNKNOWN;
            }
            JsonElement jar2 = object.get(ArchiveStreamFactory.JAR);
            if (jar2 == null) {
                object.remove("downloadJarLibraries");
            }
            if (object.has(TLauncherLib.USER_CONFIG_SKIN_VERSION)) {
                object.remove(TLauncherLib.USER_CONFIG_SKIN_VERSION);
            }
            Path modifiedVersionModel = MinecraftUtil.buildWorkingPath(PathAppUtil.VERSION_DIRECTORY, version.getID(), "TLauncherAdditional.json");
            ModifiedVersion modifiedVersion = (ModifiedVersion) this.remoteContext.fromJson((JsonElement) object, (Class<Object>) ModifiedVersion.class);
            if (Files.exists(modifiedVersionModel, new LinkOption[0])) {
                modifiedVersion = (ModifiedVersion) this.remoteContext.fromJson((JsonElement) getModifiedVersionFromJson(modifiedVersionModel), (Class<Object>) ModifiedVersion.class);
            }
            version.setModifiedVersion(modifiedVersion);
            if (version.modifiedVersion.getSource() == null) {
                version.modifiedVersion.setSource(ClientInstanceRepo.LOCAL_VERSION_REPO);
            }
            if (version.time == null) {
                version.time = new Date(0L);
            }
            if (version.assets == null) {
                version.assets = AssetIndex.DEFAULT_ASSET_NAME;
            }
            if (Objects.nonNull(version.getDownloads()) && version.getDownloads().isEmpty()) {
                version.setDownloads(null);
            }
            return version;
        }

        private JsonObject getModifiedVersionFromJson(Path absolutePath) {
            try {
                FileReader fileReader = new FileReader(absolutePath.toFile());
                JsonObject object = new JsonParser().parse(fileReader).getAsJsonObject();
                JsonObject asJsonObject = object.getAsJsonObject();
                if (fileReader != null) {
                    if (0 != 0) {
                        fileReader.close();
                    } else {
                        fileReader.close();
                    }
                }
                return asJsonObject;
            } catch (IOException e) {
                U.log("Error while getting modifiedVersion from json");
                return null;
            }
        }

        @Override // com.google.gson.JsonSerializer
        public JsonElement serialize(CompleteVersion version0, Type type, JsonSerializationContext context) {
            try {
                CompleteVersion version = (CompleteVersion) version0.clone();
                version.list = null;
                return this.remoteContext.toJsonTree(version, type);
            } catch (CloneNotSupportedException e) {
                U.log("Cloning of CompleteVersion is not supported O_o", e);
                return this.remoteContext.toJsonTree(version0, type);
            }
        }
    }

    public JavaVersionName getJavaVersion() {
        return this.javaVersion;
    }

    public void setJavaVersion(JavaVersionName javaVersion) {
        this.javaVersion = javaVersion;
    }

    @Override // net.minecraft.launcher.versions.Version
    public boolean isActivateSkinCapeForUserVersion() {
        return this.modifiedVersion.isActivateSkinCapeForUserVersion();
    }
}
