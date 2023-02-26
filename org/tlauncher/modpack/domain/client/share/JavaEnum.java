package org.tlauncher.modpack.domain.client.share;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/JavaEnum.class */
public enum JavaEnum {
    JAVA_10,
    JAVA_11,
    JAVA_9,
    JAVA_8,
    JAVA_7,
    JAVA_6;

    @JsonCreator
    public static JavaEnum create(String value) {
        return valueOf(value.toUpperCase());
    }

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return super.toString().toLowerCase();
    }
}
