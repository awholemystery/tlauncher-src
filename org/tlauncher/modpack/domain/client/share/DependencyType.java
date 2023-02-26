package org.tlauncher.modpack.domain.client.share;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/DependencyType.class */
public enum DependencyType {
    EMBEDDED,
    OPTIONAL,
    REQUIRED,
    TOOLS,
    INCOMPATIBLE,
    INCLUDED;

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return super.toString().toLowerCase();
    }

    @JsonCreator
    public static DependencyType createCategory(String value) {
        return valueOf(value.toUpperCase());
    }

    public static DependencyType[] properParserList() {
        return new DependencyType[]{REQUIRED, OPTIONAL, INCLUDED, EMBEDDED, TOOLS, INCOMPATIBLE};
    }
}
