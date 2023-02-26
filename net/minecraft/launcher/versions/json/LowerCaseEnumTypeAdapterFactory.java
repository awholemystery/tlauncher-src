package net.minecraft.launcher.versions.json;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/LowerCaseEnumTypeAdapterFactory.class */
public class LowerCaseEnumTypeAdapterFactory implements TypeAdapterFactory {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Object[] enumConstants;
        Class<? super T> rawType = type.getRawType();
        if (!rawType.isEnum()) {
            return null;
        }
        final Map<String, Object> lowercaseToConstant = new HashMap<>();
        for (Object constant : rawType.getEnumConstants()) {
            lowercaseToConstant.put(toLowercase(constant), constant);
        }
        return new TypeAdapter<T>() { // from class: net.minecraft.launcher.versions.json.LowerCaseEnumTypeAdapterFactory.1
            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter out, Object value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(LowerCaseEnumTypeAdapterFactory.toLowercase(value));
                }
            }

            /* JADX WARN: Type inference failed for: r0v4, types: [T, java.lang.Object] */
            @Override // com.google.gson.TypeAdapter
            public T read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                }
                return lowercaseToConstant.get(reader.nextString());
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String toLowercase(Object o) {
        return o.toString().toLowerCase(Locale.US);
    }
}
