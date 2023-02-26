package org.tlauncher.util.gson.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import net.minecraft.launcher.versions.json.DateTypeAdapter;
import net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;
import org.tlauncher.tlauncher.entity.PathAppUtil;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/gson/serializer/ModpackDTOTypeAdapter.class */
public class ModpackDTOTypeAdapter implements JsonSerializer<ModpackDTO>, JsonDeserializer<ModpackDTO> {
    private Gson gson;

    public ModpackDTOTypeAdapter() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new LowerCaseEnumTypeAdapterFactory());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        builder.registerTypeAdapter(ModDTO.class, new ModDTOTypeAdapter());
        builder.registerTypeAdapter(MapDTO.class, new MapDTOTypeAdapter());
        builder.registerTypeAdapter(ResourcePackDTO.class, new ResourcePackDTOTypeAdapter());
        builder.enableComplexMapKeySerialization();
        this.gson = builder.create();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public ModpackDTO deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ModpackDTO dto = (ModpackDTO) this.gson.fromJson(jsonElement, (Class<Object>) ModpackDTO.class);
        JsonObject object = jsonElement.getAsJsonObject();
        dto.setVersion((VersionDTO) this.gson.fromJson(object.get(ClientCookie.VERSION_ATTR), (Class<Object>) ModpackVersionDTO.class));
        dto.setVersions((List) this.gson.fromJson(object.get(PathAppUtil.VERSION_DIRECTORY), new TypeToken<List<ModpackVersionDTO>>() { // from class: org.tlauncher.util.gson.serializer.ModpackDTOTypeAdapter.1
        }.getType()));
        ElementCollectionsPool.fill(dto);
        return dto;
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(ModpackDTO modpackDTO, Type type, JsonSerializationContext jsonSerializationContext) {
        return this.gson.toJsonTree(modpackDTO);
    }
}
