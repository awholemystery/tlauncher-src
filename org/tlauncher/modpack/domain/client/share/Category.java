package org.tlauncher.modpack.domain.client.share;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/Category.class */
public enum Category {
    ALL,
    MAGIC,
    ADVENTURE_RPG,
    COSMETIC,
    ARMOR_WEAPONS_TOOLS,
    TECHNOLOGY,
    MAP_INFORMATION,
    LIBRARY_API,
    CHITA,
    ADDONS_FORESTRY,
    TECHNOLOGY_FARMING,
    ADDONS_THERMALEXPANSION,
    TECHNOLOGY_ENERGY,
    WORLD_MOBS,
    WORLD_BIOMES,
    SERVER_UTILITY,
    BLOOD_MAGIC,
    WORLD_DIMENSIONS,
    ADDONS_BUILDCRAFT,
    APPLIED_ENERGISTICS_2,
    TECHNOLOGY_ITEM_FLUID_ENERGY_TRANSPORT,
    TECHNOLOGY_PLAYER_TRANSPORT,
    MC_MISCELLANEOUS,
    WORLD_ORES_RESOURCES,
    MC_FOOD,
    REDSTONE,
    MC_ADDONS,
    WORLD_STRUCTURES,
    ADDONS_INDUSTRIALCRAFT,
    WORLD_GEN,
    TECHNOLOGY_PROCESSING,
    ADDONS_THAUMCRAFT,
    ADDONS_TINKERS_CONSTRUCT,
    TECHNOLOGY_GENETICS,
    ADVENTURE,
    CREATION,
    GAME_MAP,
    PARKOUR,
    SURVIVAL,
    PUZZLE,
    MODDED_WORLD,
    SIXTEEN_X,
    THIRTY_TWO_X,
    SIXTY_FOUR_X,
    ONE_TWENTY_EIGHT_X,
    TWO_FIFTY_SIX_X,
    FIVE_TWELVE_X_AND_BEYOND,
    ANIMATED,
    MEDIEVAL,
    MOD_SUPPORT,
    MODERN,
    PHOTO_REALISTIC,
    STEAMPUNK,
    TRADITIONAL,
    MISCELLANEOUS,
    ADVENTURE_AND_RPG,
    COMBAT_PVP,
    FTB_OFFICIAL_PACK,
    HARDCORE,
    MAP_BASED,
    MINI_GAME,
    MULTIPLAYER,
    QUESTS,
    SCI_FI,
    TECH,
    EXPLORATION,
    EXTRA_LARGE,
    SMALL_LIGHT,
    LUCKY_BLOCKS,
    CRAFTTWEAKER,
    HARDCORE_QUESTING_MODE,
    CONFIGURATION,
    SCRIPTS,
    RECIPES,
    BUILDING_GADGETS,
    PROGRESSION,
    GUIDEBOOK,
    REALISTIC,
    LAG_LESS,
    LOW_END,
    PSYCHEDELIC,
    CUSTOMIZATION,
    SHADERPACKS,
    STORAGE,
    FABRIC,
    TWITCH_INTEGRATION,
    SKYBLOCK;
    
    public static final Map<Category, Integer> common = new HashMap<Category, Integer>() { // from class: org.tlauncher.modpack.domain.client.share.Category.1
        {
            put(Category.TECHNOLOGY_ENERGY, 2);
            put(Category.TECHNOLOGY_ENERGY, 2);
            put(Category.TECHNOLOGY_ITEM_FLUID_ENERGY_TRANSPORT, 2);
            put(Category.TECHNOLOGY_FARMING, 2);
            put(Category.TECHNOLOGY_GENETICS, 2);
            put(Category.TECHNOLOGY_PLAYER_TRANSPORT, 2);
            put(Category.TECHNOLOGY_PROCESSING, 2);
            put(Category.WORLD_BIOMES, 2);
            put(Category.WORLD_DIMENSIONS, 2);
            put(Category.WORLD_MOBS, 2);
            put(Category.WORLD_ORES_RESOURCES, 2);
            put(Category.WORLD_STRUCTURES, 2);
            put(Category.APPLIED_ENERGISTICS_2, 2);
            put(Category.BLOOD_MAGIC, 2);
            put(Category.ADDONS_BUILDCRAFT, 2);
            put(Category.ADDONS_FORESTRY, 2);
            put(Category.ADDONS_INDUSTRIALCRAFT, 2);
            put(Category.ADDONS_THAUMCRAFT, 2);
            put(Category.ADDONS_THERMALEXPANSION, 2);
            put(Category.ADDONS_TINKERS_CONSTRUCT, 2);
            put(Category.HARDCORE_QUESTING_MODE, 2);
            put(Category.PROGRESSION, 2);
            put(Category.CRAFTTWEAKER, 2);
            put(Category.RECIPES, 2);
            put(Category.BUILDING_GADGETS, 2);
            put(Category.CONFIGURATION, 2);
            put(Category.GUIDEBOOK, 2);
        }
    };
    public static final Map<Category, Integer> shaderMap = new HashMap<Category, Integer>() { // from class: org.tlauncher.modpack.domain.client.share.Category.2
        {
            put(Category.CONFIGURATION, 2);
            put(Category.LUCKY_BLOCKS, 3);
            put(Category.GUIDEBOOK, 2);
            put(Category.QUESTS, 2);
            put(Category.HARDCORE_QUESTING_MODE, 3);
            put(Category.PROGRESSION, 3);
            put(Category.SCRIPTS, 2);
            put(Category.CRAFTTWEAKER, 3);
            put(Category.RECIPES, 3);
            put(Category.WORLD_GEN, 3);
            put(Category.BUILDING_GADGETS, 3);
            put(Category.REALISTIC, 2);
            put(Category.LAG_LESS, 2);
            put(Category.LOW_END, 2);
            put(Category.PSYCHEDELIC, 2);
        }
    };
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static Category[] valuesCustom() {
        Category[] valuesCustom = values();
        int length = valuesCustom.length;
        Category[] categoryArr = new Category[length];
        System.arraycopy(valuesCustom, 0, categoryArr, 0, length);
        return categoryArr;
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

    @JsonCreator
    public static Category createCategory(String value) {
        return valueOf(value.toUpperCase());
    }

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    /* renamed from: org.tlauncher.modpack.domain.client.share.Category$3  reason: invalid class name */
    /* loaded from: TLauncher-2.876.jar:org/tlauncher/modpack/domain/client/share/Category$3.class */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType = new int[GameType.valuesCustom().length];

        static {
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MODPACK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MOD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.MAP.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.RESOURCEPACK.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$tlauncher$modpack$domain$client$share$GameType[GameType.SHADERPACK.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static Category[] getCategories(GameType type) {
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
                return new Category[]{ALL, ADVENTURE, CREATION, GAME_MAP, MODDED_WORLD, PARKOUR, PUZZLE, SURVIVAL};
            case 2:
                return new Category[]{ALL, ADVENTURE_RPG, ARMOR_WEAPONS_TOOLS, COSMETIC, MC_FOOD, MAGIC, MAP_INFORMATION, REDSTONE, SERVER_UTILITY, TECHNOLOGY, TECHNOLOGY_ENERGY, TECHNOLOGY_ITEM_FLUID_ENERGY_TRANSPORT, TECHNOLOGY_FARMING, TECHNOLOGY_GENETICS, TECHNOLOGY_PLAYER_TRANSPORT, TECHNOLOGY_PROCESSING, WORLD_GEN, WORLD_BIOMES, WORLD_DIMENSIONS, WORLD_MOBS, WORLD_ORES_RESOURCES, WORLD_STRUCTURES, MC_ADDONS, APPLIED_ENERGISTICS_2, BLOOD_MAGIC, ADDONS_BUILDCRAFT, ADDONS_FORESTRY, ADDONS_INDUSTRIALCRAFT, ADDONS_THAUMCRAFT, ADDONS_THERMALEXPANSION, ADDONS_TINKERS_CONSTRUCT, LIBRARY_API, MC_MISCELLANEOUS, CHITA, FABRIC, TWITCH_INTEGRATION, STORAGE};
            case 3:
                return new Category[]{ALL, ADVENTURE_AND_RPG, COMBAT_PVP, EXPLORATION, EXTRA_LARGE, FTB_OFFICIAL_PACK, HARDCORE, MAGIC, MAP_BASED, MINI_GAME, MULTIPLAYER, QUESTS, SCI_FI, SMALL_LIGHT, TECH, SKYBLOCK};
            case 4:
                return new Category[]{ALL, SIXTEEN_X, THIRTY_TWO_X, SIXTY_FOUR_X, ONE_TWENTY_EIGHT_X, TWO_FIFTY_SIX_X, FIVE_TWELVE_X_AND_BEYOND, ANIMATED, MEDIEVAL, MOD_SUPPORT, MODERN, PHOTO_REALISTIC, STEAMPUNK, TRADITIONAL, MISCELLANEOUS};
            case 5:
            default:
                return null;
            case 6:
                return new Category[]{ALL, SHADERPACKS, REALISTIC, LAG_LESS, LOW_END, PSYCHEDELIC, CUSTOMIZATION, CONFIGURATION, LUCKY_BLOCKS, GUIDEBOOK, QUESTS, HARDCORE_QUESTING_MODE, PROGRESSION, SCRIPTS, CRAFTTWEAKER, RECIPES, WORLD_GEN, BUILDING_GADGETS};
        }
    }

    public static Map<Category, Integer> getSubCategories(GameType type) {
        switch ($SWITCH_TABLE$org$tlauncher$modpack$domain$client$share$GameType()[type.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
                return common;
            case 5:
            default:
                return new HashMap();
            case 6:
                return shaderMap;
        }
    }
}
