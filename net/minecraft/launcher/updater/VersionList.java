package net.minecraft.launcher.updater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import net.minecraft.launcher.versions.CompleteVersion;
import net.minecraft.launcher.versions.ModifiedVersion;
import net.minecraft.launcher.versions.PartialVersion;
import net.minecraft.launcher.versions.ReleaseType;
import net.minecraft.launcher.versions.Version;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
import org.tlauncher.serialization.version.ModifiedVersionSerializer;
import org.tlauncher.util.OS;
import org.tlauncher.util.Time;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/VersionList.class */
public abstract class VersionList {
    public final Gson gson;
    protected final List<Version> versions = Collections.synchronizedList(new ArrayList());
    protected final Map<String, Version> byName = new Hashtable();
    protected final Map<ReleaseType, Version> latest = new Hashtable();

    public abstract boolean hasAllFiles(CompleteVersion completeVersion, OS os);

    protected abstract String getUrl(String str) throws IOException;

    /* JADX INFO: Access modifiers changed from: package-private */
    public VersionList() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        builder.registerTypeAdapter(CompleteVersion.class, new CompleteVersion.CompleteVersionSerializer());
        builder.registerTypeAdapter(ModifiedVersion.class, new ModifiedVersionSerializer());
        builder.enableComplexMapKeySerialization();
        builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        this.gson = builder.create();
    }

    public List<Version> getVersions() {
        return this.versions;
    }

    public Map<ReleaseType, Version> getLatestVersions() {
        return this.latest;
    }

    public Version getVersion(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be NULL or empty");
        }
        return this.byName.get(name);
    }

    public CompleteVersion getCompleteVersion(Version version) throws JsonSyntaxException, IOException {
        if (version instanceof CompleteVersion) {
            return (CompleteVersion) version;
        }
        if (version == null) {
            throw new NullPointerException("Version cannot be NULL!");
        }
        CompleteVersion complete = (CompleteVersion) this.gson.fromJson(getUrl("versions/" + version.getID() + "/" + version.getID() + ".json"), (Class<Object>) CompleteVersion.class);
        complete.setID(version.getID());
        complete.setVersionList(this);
        complete.setSkinVersion(version.isSkinVersion());
        Collections.replaceAll(this.versions, version, complete);
        return complete;
    }

    public CompleteVersion getCompleteVersion(String name) throws JsonSyntaxException, IOException {
        Version version = getVersion(name);
        if (version == null) {
            return null;
        }
        return getCompleteVersion(version);
    }

    public Version getLatestVersion(ReleaseType type) {
        if (type == null) {
            throw new NullPointerException();
        }
        return this.latest.get(type);
    }

    public RawVersionList getRawList() throws IOException {
        Object lock = new Object();
        Time.start(lock);
        RawVersionList list = (RawVersionList) this.gson.fromJson(getUrl("versions/versions.json"), (Class<Object>) RawVersionList.class);
        for (PartialVersion version : list.versions) {
            version.setVersionList(this);
        }
        log("Got in", Long.valueOf(Time.stop(lock)), "ms");
        return list;
    }

    public void refreshVersions(RawVersionList versionList) {
        clearCache();
        for (Version version : versionList.getVersions()) {
            if (version != null && version.getID() != null) {
                this.versions.add(version);
                this.byName.put(version.getID(), version);
            }
        }
        for (Map.Entry<ReleaseType, String> en : versionList.latest.entrySet()) {
            ReleaseType releaseType = en.getKey();
            if (releaseType == null) {
                log("Unknown release type for latest version entry:", en);
            } else {
                Version version2 = getVersion(en.getValue());
                if (version2 == null) {
                    throw new NullPointerException("Cannot find version for latest version entry: " + en);
                }
                this.latest.put(releaseType, version2);
            }
        }
    }

    public void refreshVersions() throws IOException {
        refreshVersions(getRawList());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CompleteVersion addVersion(CompleteVersion version) {
        if (version.getID() == null) {
            throw new IllegalArgumentException("Cannot add blank version");
        }
        if (getVersion(version.getID()) != null) {
            log("Version '" + version.getID() + "' is already tracked");
            return version;
        }
        this.versions.add(version);
        this.byName.put(version.getID(), version);
        return version;
    }

    public JsonElement serializeVersion(CompleteVersion version) {
        if (version == null) {
            throw new NullPointerException("CompleteVersion cannot be NULL!");
        }
        return this.gson.toJsonTree(version);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearCache() {
        this.byName.clear();
        this.versions.clear();
        this.latest.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void log(Object... obj) {
        U.log("[" + getClass().getSimpleName() + "]", obj);
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/VersionList$RawVersionList.class */
    public static class RawVersionList {
        List<PartialVersion> versions = new ArrayList();
        Map<ReleaseType, String> latest = new EnumMap(ReleaseType.class);

        public List<PartialVersion> getVersions() {
            return this.versions;
        }

        public Map<ReleaseType, String> getLatestVersions() {
            return this.latest;
        }
    }
}
