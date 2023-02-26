package org.tlauncher.serialization.version;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Date;
import net.minecraft.launcher.versions.ModifiedVersion;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import net.minecraft.launcher.versions.json.RepoTypeAdapter;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.tlauncher.entity.TLauncherLib;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/serialization/version/ModifiedVersionSerializer.class */
public class ModifiedVersionSerializer implements JsonSerializer<ModifiedVersion>, JsonDeserializer<ModifiedVersion> {
    private final Gson defaultContext;

    public ModifiedVersionSerializer() {
        GsonBuilder remoteBuilder = new GsonBuilder();
        remoteBuilder.registerTypeAdapter(Repo.class, new RepoTypeAdapter());
        remoteBuilder.registerTypeAdapter(ModpackDTO.class, new ModpackDTOTypeAdapter());
        remoteBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        remoteBuilder.enableComplexMapKeySerialization();
        remoteBuilder.disableHtmlEscaping();
        remoteBuilder.setPrettyPrinting();
        this.defaultContext = remoteBuilder.create();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public ModifiedVersion deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        return (ModifiedVersion) this.defaultContext.fromJson((JsonElement) object, (Class<Object>) ModifiedVersion.class);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(ModifiedVersion modifiedVersion, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonElement = (JsonObject) this.defaultContext.toJsonTree(modifiedVersion, type);
        JsonElement jar = jsonElement.get(ArchiveStreamFactory.JAR);
        if (jar == null) {
            jsonElement.remove("downloadJarLibraries");
        }
        if (jsonElement.has(TLauncherLib.USER_CONFIG_SKIN_VERSION)) {
            jsonElement.remove(TLauncherLib.USER_CONFIG_SKIN_VERSION);
        }
        return jsonElement;
    }
}
