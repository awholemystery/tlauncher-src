package net.minecraft.launcher.versions.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/PatternTypeAdapter.class */
public class PatternTypeAdapter implements JsonSerializer<Pattern>, JsonDeserializer<Pattern> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Pattern deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String string = json.getAsString();
        if (StringUtils.isBlank(string)) {
            return null;
        }
        return Pattern.compile(string);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Pattern src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src == null ? null : src.toString());
    }
}
