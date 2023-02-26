package org.tlauncher.modpack.domain.client.share;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/TopicType.class */
public enum TopicType {
    GAME_ENTITY,
    SUB_COMMENT;

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
