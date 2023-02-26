package org.tlauncher.modpack.domain.client.version;

import java.util.ArrayList;
import java.util.List;
import org.tlauncher.modpack.domain.client.ForgeVersionDTO;
import org.tlauncher.modpack.domain.client.GameEntityDTO;
import org.tlauncher.modpack.domain.client.GameVersionDTO;
import org.tlauncher.modpack.domain.client.MapDTO;
import org.tlauncher.modpack.domain.client.ModDTO;
import org.tlauncher.modpack.domain.client.ModpackDTO;
import org.tlauncher.modpack.domain.client.ResourcePackDTO;
import org.tlauncher.modpack.domain.client.ShaderpackDTO;
import org.tlauncher.modpack.domain.client.share.GameType;
import org.tlauncher.modpack.domain.client.share.NameIdDTO;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/version/ModpackVersionDTO.class */
public class ModpackVersionDTO extends VersionDTO {
    private List<ModDTO> mods = new ArrayList();
    private List<ResourcePackDTO> resourcePacks = new ArrayList();
    private List<MapDTO> maps = new ArrayList();
    private List<ShaderpackDTO> shaderpacks = new ArrayList();
    private MetadataDTO additionalFile;
    private String forgeVersion;
    private String gameVersion;
    private ForgeVersionDTO forgeVersionDTO;
    private GameVersionDTO gameVersionDTO;
    private NameIdDTO minecraftVersionName;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType;

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

    public MetadataDTO getAdditionalFile() {
        return this.additionalFile;
    }

    public String getForgeVersion() {
        return this.forgeVersion;
    }

    public String getGameVersion() {
        return this.gameVersion;
    }

    public ForgeVersionDTO getForgeVersionDTO() {
        return this.forgeVersionDTO;
    }

    public GameVersionDTO getGameVersionDTO() {
        return this.gameVersionDTO;
    }

    public NameIdDTO getMinecraftVersionName() {
        return this.minecraftVersionName;
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

    public void setAdditionalFile(MetadataDTO additionalFile) {
        this.additionalFile = additionalFile;
    }

    public void setForgeVersion(String forgeVersion) {
        this.forgeVersion = forgeVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public void setForgeVersionDTO(ForgeVersionDTO forgeVersionDTO) {
        this.forgeVersionDTO = forgeVersionDTO;
    }

    public void setGameVersionDTO(GameVersionDTO gameVersionDTO) {
        this.gameVersionDTO = gameVersionDTO;
    }

    public void setMinecraftVersionName(NameIdDTO minecraftVersionName) {
        this.minecraftVersionName = minecraftVersionName;
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ModpackVersionDTO) {
            ModpackVersionDTO other = (ModpackVersionDTO) o;
            if (other.canEqual(this) && super.equals(o)) {
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
                if (this$shaderpacks == null) {
                    if (other$shaderpacks != null) {
                        return false;
                    }
                } else if (!this$shaderpacks.equals(other$shaderpacks)) {
                    return false;
                }
                Object this$additionalFile = getAdditionalFile();
                Object other$additionalFile = other.getAdditionalFile();
                if (this$additionalFile == null) {
                    if (other$additionalFile != null) {
                        return false;
                    }
                } else if (!this$additionalFile.equals(other$additionalFile)) {
                    return false;
                }
                Object this$forgeVersion = getForgeVersion();
                Object other$forgeVersion = other.getForgeVersion();
                if (this$forgeVersion == null) {
                    if (other$forgeVersion != null) {
                        return false;
                    }
                } else if (!this$forgeVersion.equals(other$forgeVersion)) {
                    return false;
                }
                Object this$gameVersion = getGameVersion();
                Object other$gameVersion = other.getGameVersion();
                if (this$gameVersion == null) {
                    if (other$gameVersion != null) {
                        return false;
                    }
                } else if (!this$gameVersion.equals(other$gameVersion)) {
                    return false;
                }
                Object this$forgeVersionDTO = getForgeVersionDTO();
                Object other$forgeVersionDTO = other.getForgeVersionDTO();
                if (this$forgeVersionDTO == null) {
                    if (other$forgeVersionDTO != null) {
                        return false;
                    }
                } else if (!this$forgeVersionDTO.equals(other$forgeVersionDTO)) {
                    return false;
                }
                Object this$gameVersionDTO = getGameVersionDTO();
                Object other$gameVersionDTO = other.getGameVersionDTO();
                if (this$gameVersionDTO == null) {
                    if (other$gameVersionDTO != null) {
                        return false;
                    }
                } else if (!this$gameVersionDTO.equals(other$gameVersionDTO)) {
                    return false;
                }
                Object this$minecraftVersionName = getMinecraftVersionName();
                Object other$minecraftVersionName = other.getMinecraftVersionName();
                return this$minecraftVersionName == null ? other$minecraftVersionName == null : this$minecraftVersionName.equals(other$minecraftVersionName);
            }
            return false;
        }
        return false;
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    protected boolean canEqual(Object other) {
        return other instanceof ModpackVersionDTO;
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    public int hashCode() {
        int result = super.hashCode();
        Object $mods = getMods();
        int result2 = (result * 59) + ($mods == null ? 43 : $mods.hashCode());
        Object $resourcePacks = getResourcePacks();
        int result3 = (result2 * 59) + ($resourcePacks == null ? 43 : $resourcePacks.hashCode());
        Object $maps = getMaps();
        int result4 = (result3 * 59) + ($maps == null ? 43 : $maps.hashCode());
        Object $shaderpacks = getShaderpacks();
        int result5 = (result4 * 59) + ($shaderpacks == null ? 43 : $shaderpacks.hashCode());
        Object $additionalFile = getAdditionalFile();
        int result6 = (result5 * 59) + ($additionalFile == null ? 43 : $additionalFile.hashCode());
        Object $forgeVersion = getForgeVersion();
        int result7 = (result6 * 59) + ($forgeVersion == null ? 43 : $forgeVersion.hashCode());
        Object $gameVersion = getGameVersion();
        int result8 = (result7 * 59) + ($gameVersion == null ? 43 : $gameVersion.hashCode());
        Object $forgeVersionDTO = getForgeVersionDTO();
        int result9 = (result8 * 59) + ($forgeVersionDTO == null ? 43 : $forgeVersionDTO.hashCode());
        Object $gameVersionDTO = getGameVersionDTO();
        int result10 = (result9 * 59) + ($gameVersionDTO == null ? 43 : $gameVersionDTO.hashCode());
        Object $minecraftVersionName = getMinecraftVersionName();
        return (result10 * 59) + ($minecraftVersionName == null ? 43 : $minecraftVersionName.hashCode());
    }

    @Override // org.tlauncher.modpack.domain.client.version.VersionDTO
    public String toString() {
        return "ModpackVersionDTO(super=" + super.toString() + ", mods=" + getMods() + ", resourcePacks=" + getResourcePacks() + ", maps=" + getMaps() + ", shaderpacks=" + getShaderpacks() + ", additionalFile=" + getAdditionalFile() + ", forgeVersion=" + getForgeVersion() + ", gameVersion=" + getGameVersion() + ", forgeVersionDTO=" + getForgeVersionDTO() + ", gameVersionDTO=" + getGameVersionDTO() + ", minecraftVersionName=" + getMinecraftVersionName() + ")";
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

    /* renamed from: org.tlauncher.modpack.domain.client.version.ModpackVersionDTO$1  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/version/ModpackVersionDTO$1.class */
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
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.SHADERPACK.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public List<? extends GameEntityDTO> getByType(GameType type) {
        List<? extends GameEntityDTO> list = new ArrayList<>();
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
                list = this.maps;
                break;
            case 2:
                list = this.mods;
                break;
            case 4:
                list = this.resourcePacks;
                break;
            case 6:
                list = this.shaderpacks;
                break;
        }
        return list;
    }

    public void add(GameType type, GameEntityDTO e) {
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
                this.maps.add((MapDTO) e);
                return;
            case 2:
                this.mods.add((ModDTO) e);
                return;
            case 3:
            case 5:
            default:
                return;
            case 4:
                this.resourcePacks.add((ResourcePackDTO) e);
                return;
            case 6:
                this.shaderpacks.add((ShaderpackDTO) e);
                return;
        }
    }

    public ModpackVersionDTO copy(ModpackVersionDTO version) {
        version.setId(getId());
        version.setForgeVersionDTO(getForgeVersionDTO());
        version.setGameVersionDTO(getGameVersionDTO());
        version.setAdditionalFile(this.additionalFile);
        version.setGameVersions(getGameVersions());
        version.setMaps(new ModpackDTO.CheckNullList(this.maps));
        version.setMods(new ModpackDTO.CheckNullList(this.mods));
        version.setShaderpacks(new ModpackDTO.CheckNullList(this.shaderpacks));
        version.setName(getName());
        version.setResourcePacks(new ModpackDTO.CheckNullList(this.resourcePacks));
        version.setType(getType());
        version.setUpdatedDate(getUpdatedDate());
        version.setJavaVersions(getJavaVersions());
        version.setMinecraftVersionTypes(getMinecraftVersionTypes());
        version.setMinecraftVersionName(getMinecraftVersionName());
        return version;
    }
}
