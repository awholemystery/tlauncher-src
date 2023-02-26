package org.tlauncher.modpack.domain.client.share;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/StateGameElement.class */
public enum StateGameElement {
    NO_ACTIVE,
    ACTIVE,
    BLOCK;

    @JsonCreator
    public static StateGameElement createCategory(String value) {
        return valueOf(value.toUpperCase(Locale.ROOT));
    }

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
