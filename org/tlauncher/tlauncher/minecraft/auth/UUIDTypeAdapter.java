package org.tlauncher.tlauncher.minecraft.auth;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.UUID;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/UUIDTypeAdapter.class */
public class UUIDTypeAdapter extends TypeAdapter<UUID> {
    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, UUID value) throws IOException {
        out.value(fromUUID(value));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    public UUID read(JsonReader in) throws IOException {
        return fromString(in.nextString());
    }

    public static String toUUID(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("-", CoreConstants.EMPTY_STRING);
    }

    public static String fromUUID(UUID value) {
        return toUUID(value.toString());
    }

    public static UUID fromString(String input) {
        return UUID.fromString(input.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }
}
