package org.tlauncher.modpack.domain.client.share;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/Type.class */
public enum Type {
    BETA,
    RELEASE;

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String toWebParam() {
        return name().toUpperCase(Locale.ROOT);
    }

    @JsonCreator
    public static Type createCategory(String value) {
        return valueOf(value.toUpperCase());
    }
}
