package org.tlauncher.modpack.domain.client.share;

import ch.qos.logback.core.CoreConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.ShaderpackDTO;
import org.tlauncher.modpack.domain.client.version.ModVersionDTO;
import org.tlauncher.modpack.domain.client.version.ModpackVersionDTO;
import org.tlauncher.modpack.domain.client.version.VersionDTO;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/GameType.class */
public enum GameType {
    MAP,
    MOD,
    MODPACK,
    RESOURCEPACK,
    NOT_MODPACK,
    SHADERPACK;
    
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static GameType[] valuesCustom() {
        GameType[] valuesCustom = values();
        int length = valuesCustom.length;
        GameType[] gameTypeArr = new GameType[length];
        System.arraycopy(valuesCustom, 0, gameTypeArr, 0, length);
        return gameTypeArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType() {
        int[] iArr = $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[valuesCustom().length];
        try {
            iArr2[MAP.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[MOD.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[MODPACK.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[NOT_MODPACK.ordinal()] = 5;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[RESOURCEPACK.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[SHADERPACK.ordinal()] = 6;
        } catch (NoSuchFieldError unused6) {
        }
        $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType = iArr2;
        return iArr2;
    }

    @JsonValue
    public String toLowerCase() {
        return name().toLowerCase(Locale.ROOT);
    }

    public String toWebParam() {
        return name().toUpperCase(Locale.ROOT);
    }

    @JsonCreator
    public static GameType create(String value) {
        return valueOf(value.toUpperCase());
    }

    /* renamed from: org.tlauncher.modpack.domain.client.share.GameType$3  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/GameType$3.class */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType = new int[GameType.valuesCustom().length];

        static {
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MAP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MOD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.RESOURCEPACK.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MODPACK.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.SHADERPACK.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static Class<? extends GameEntityDTO> createDTO(GameType type) {
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
                return MapDTO.class;
            case 2:
                return ModDTO.class;
            case 3:
                return ModpackDTO.class;
            case 4:
                return ResourcePackDTO.class;
            case 5:
            default:
                throw new NullPointerException();
            case 6:
                return ShaderpackDTO.class;
        }
    }

    public static Class<? extends VersionDTO> createVersionDTO(GameType type) {
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
                return VersionDTO.class;
            case 2:
                return ModVersionDTO.class;
            case 3:
                return ModpackVersionDTO.class;
            case 4:
                return VersionDTO.class;
            case 5:
            default:
                throw new NullPointerException();
            case 6:
                return VersionDTO.class;
        }
    }

    public static GameType create(Class<? extends GameEntityDTO> c) {
        return create(c.getSimpleName().replaceAll("DTO", CoreConstants.EMPTY_STRING));
    }

    public static List<GameType> getSubEntities() {
        return new ArrayList<GameType>() { // from class: org.tlauncher.modpack.domain.client.share.GameType.1
            {
                add(GameType.MOD);
                add(GameType.MAP);
                add(GameType.RESOURCEPACK);
                add(GameType.SHADERPACK);
            }
        };
    }

    public static List<String> getPluralWords() {
        return (List) getExistedGameTypes().stream().map(GameType::getPluralStringWord).collect(Collectors.toList());
    }

    public static String getPluralStringWord(GameType type) {
        return String.valueOf(type.name().toLowerCase(Locale.ROOT)) + "s";
    }

    public static List<GameType> getExistedGameTypes() {
        return new ArrayList<GameType>() { // from class: org.tlauncher.modpack.domain.client.share.GameType.2
            {
                add(GameType.MOD);
                add(GameType.MODPACK);
                add(GameType.RESOURCEPACK);
                add(GameType.MAP);
                add(GameType.SHADERPACK);
            }
        };
    }
}
