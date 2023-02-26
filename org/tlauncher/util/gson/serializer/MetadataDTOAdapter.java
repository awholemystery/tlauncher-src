package org.tlauncher.util.gson.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.tlauncher.modpack.domain.client.version.MapMetadataDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/gson/serializer/MetadataDTOAdapter.class */
public class MetadataDTOAdapter implements JsonSerializer<MapMetadataDTO>, JsonDeserializer<MapMetadataDTO> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public MapMetadataDTO deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        MapMetadataDTO meta = (MapMetadataDTO) context.deserialize(json, MapMetadataDTO.class);
        return meta;
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(MapMetadataDTO src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src);
    }
}
