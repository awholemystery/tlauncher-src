package org.tlauncher.modpack.domain.client.share;

import java.util.ArrayList;
import java.util.List;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.ShaderpackDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/InfoMod.class */
public class InfoMod {
    List<GameVersionDTO> gameVersions = new ArrayList();
    List<ModpackDTO> modPacks = new ArrayList();
    List<ModDTO> mods = new ArrayList();
    List<ResourcePackDTO> resourcePacks = new ArrayList();
    List<MapDTO> maps = new ArrayList();
    List<ShaderpackDTO> shaderpacks = new ArrayList();
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType;

    public List<GameVersionDTO> getGameVersions() {
        return this.gameVersions;
    }

    public List<ModpackDTO> getModPacks() {
        return this.modPacks;
    }

    public List<ModDTO> getMods() {
        return this.mods;
    }

    public List<ResourcePackDTO> getResourcePacks() {
        return this.resourcePacks;
    }

    public List<MapDTO> getMaps() {
        return this.maps;
    }

    public List<ShaderpackDTO> getShaderpacks() {
        return this.shaderpacks;
    }

    public void setGameVersions(List<GameVersionDTO> gameVersions) {
        this.gameVersions = gameVersions;
    }

    public void setModPacks(List<ModpackDTO> modPacks) {
        this.modPacks = modPacks;
    }

    public void setMods(List<ModDTO> mods) {
        this.mods = mods;
    }

    public void setResourcePacks(List<ResourcePackDTO> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }

    public void setMaps(List<MapDTO> maps) {
        this.maps = maps;
    }

    public void setShaderpacks(List<ShaderpackDTO> shaderpacks) {
        this.shaderpacks = shaderpacks;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof InfoMod) {
            InfoMod other = (InfoMod) o;
            if (other.canEqual(this)) {
                Object this$gameVersions = getGameVersions();
                Object other$gameVersions = other.getGameVersions();
                if (this$gameVersions == null) {
                    if (other$gameVersions != null) {
                        return false;
                    }
                } else if (!this$gameVersions.equals(other$gameVersions)) {
                    return false;
                }
                Object this$modPacks = getModPacks();
                Object other$modPacks = other.getModPacks();
                if (this$modPacks == null) {
                    if (other$modPacks != null) {
                        return false;
                    }
                } else if (!this$modPacks.equals(other$modPacks)) {
                    return false;
                }
                Object this$mods = getMods();
                Object other$mods = other.getMods();
                if (this$mods == null) {
                    if (other$mods != null) {
                        return false;
                    }
                } else if (!this$mods.equals(other$mods)) {
                    return false;
                }
                Object this$resourcePacks = getResourcePacks();
                Object other$resourcePacks = other.getResourcePacks();
                if (this$resourcePacks == null) {
                    if (other$resourcePacks != null) {
                        return false;
                    }
                } else if (!this$resourcePacks.equals(other$resourcePacks)) {
                    return false;
                }
                Object this$maps = getMaps();
                Object other$maps = other.getMaps();
                if (this$maps == null) {
                    if (other$maps != null) {
                        return false;
                    }
                } else if (!this$maps.equals(other$maps)) {
                    return false;
                }
                Object this$shaderpacks = getShaderpacks();
                Object other$shaderpacks = other.getShaderpacks();
                return this$shaderpacks == null ? other$shaderpacks == null : this$shaderpacks.equals(other$shaderpacks);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof InfoMod;
    }

    public int hashCode() {
        Object $gameVersions = getGameVersions();
        int result = (1 * 59) + ($gameVersions == null ? 43 : $gameVersions.hashCode());
        Object $modPacks = getModPacks();
        int result2 = (result * 59) + ($modPacks == null ? 43 : $modPacks.hashCode());
        Object $mods = getMods();
        int result3 = (result2 * 59) + ($mods == null ? 43 : $mods.hashCode());
        Object $resourcePacks = getResourcePacks();
        int result4 = (result3 * 59) + ($resourcePacks == null ? 43 : $resourcePacks.hashCode());
        Object $maps = getMaps();
        int result5 = (result4 * 59) + ($maps == null ? 43 : $maps.hashCode());
        Object $shaderpacks = getShaderpacks();
        return (result5 * 59) + ($shaderpacks == null ? 43 : $shaderpacks.hashCode());
    }

    public String toString() {
        return "InfoMod(gameVersions=" + getGameVersions() + ", modPacks=" + getModPacks() + ", mods=" + getMods() + ", resourcePacks=" + getResourcePacks() + ", maps=" + getMaps() + ", shaderpacks=" + getShaderpacks() + ")";
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType() {
        int[] iArr = $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[GameType.valuesCustom().length];
        try {
            iArr2[GameType.MAP.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[GameType.MOD.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[GameType.MODPACK.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[GameType.NOT_MODPACK.ordinal()] = 5;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[GameType.RESOURCEPACK.ordinal()] = 4;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[GameType.SHADERPACK.ordinal()] = 6;
        } catch (NoSuchFieldError unused6) {
        }
        $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType = iArr2;
        return iArr2;
    }

    /* renamed from: org.tlauncher.modpack.domain.client.share.InfoMod$1  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/InfoMod$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType = new int[GameType.valuesCustom().length];

        static {
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MOD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MAP.ordinal()] = 2;
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

    public List<? extends GameEntityDTO> getByType(GameType type) {
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
                return this.maps;
            case 2:
                return this.mods;
            case 3:
                return this.modPacks;
            case 4:
                return this.resourcePacks;
            case 5:
            default:
                return null;
            case 6:
                return this.shaderpacks;
        }
    }
}
