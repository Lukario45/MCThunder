package net.mcthunder.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Material {//http://minecraft.gamepedia.com/Id
    AIR("AIR", 0),
    STONE("STONE", 1),
    GRASS("GRASS", 2),
    DIRT("DIRT", 3),
    COBBLESTONE("COBBLESTONE", 4, Arrays.asList("COBBLE")),
    PLANKS("PLANKS", 5),
    SAPLING("SAPLING", 6),
    BEDROCK("BEDROCK", 7, Arrays.asList("ADMINIUM")),
    FLOWING_WATER("FLOWING_WATER", 8),
    WATER("WATER", 9),
    FLOWING_LAVA("FLOWING_LAVA", 10),
    LAVA("LAVA", 11),
    SAND("SAND", 12),
    GRAVEL("GRAVEL", 13),
    GOLD_ORE("GOLD_ORE", 14),
    IRON_ORE("IRON_ORE", 15),
    COAL_ORE("COAL_ORE", 16),
    LOG("LOG", 17),
    LEAVES("LEAVES", 18),
    SPONGE("SPONGE", 19),
    GLASS("GLASS", 20),
    LAPIS_ORE("LAPIS_ORE", 21),
    LAPIS_BLOCK("LAPIS_BLOCK", 22),
    DISPENSER("DISPENSER", 23),
    SANDSTONE("SANDSTONE", 24),
    NOTEBLOCK("NOTEBLOCK", 25),
    BED_BLOCK("BED_BLOCK", 26),//Unobtainable
    GOLDEN_RAIL("GOLDEN_RAIL", 27, Arrays.asList("POWERED_RAIL")),
    DETECTOR_RAIL("DETECTOR_RAIL", 28),
    STICKY_PISTON("STICKY_PISTON", 29),
    WEB("WEB", 30, Arrays.asList("COBWEB")),
    TALLGRASS("TALLGRASS", 31),
    DEADBUSH("DEADBUSH", 32),
    PISTON("PISTON", 33),
    PISTON_HEAD("PISTON_HEAD", 34),//Unobtainable
    WOOL("WOOL", 35),
    PISTON_EXTENSION("PISTON_EXTENSION", 36),//Unobtainable
    YELLOW_FLOWER("YELLOW_FLOWER", 37, Arrays.asList("DANDELION")),
    RED_FLOWER("RED_FLOWER", 38, Arrays.asList("POPPY")),
    BROWN_MUSHROOM("BROWN_MUSHROOM", 39),
    RED_MUSHROOM("RED_MUSHROOM", 40),
    GOLD_BLOCK("GOLD_BLOCK", 41, Arrays.asList("BLOCK_OF_GOLD")),
    IRON_BLOCK("IRON_BLOCK", 42, Arrays.asList("BLOCK_OF_IRON")),
    DOUBLE_STONE_SLAB("DOUBLE_STONE_SLAB", 43),//Unobtainable
    STONE_SLAB("STONE_SLAB", 44),
    BRICK_BLOCK("BRICK_BLOCK", 45),
    TNT("TNT", 46),
    BOOKSHELF("BOOKSHELF", 47),
    MOSSY_COBBLESTONE("MOSSY_COBBLESTONE", 48),
    OBSIDIAN("OBSIDIAN", 49),
    TORCH("TORCH", 50),
    FIRE("FIRE", 51),//Unobtainable
    MOB_SPAWNER("MOB_SPAWNER", 52),
    OAK_STAIRS("OAK_STAIRS", 53),
    CHEST("CHEST", 54),
    REDSTONE_WIRE("REDSTONE_WIRE", 55),//Unobtainable
    DIAMOND_ORE("DIAMOND_ORE", 56),
    DIAMOND_BLOCK("DIAMOND_BLOCK", 57),
    CRAFTING_TABLE("CRAFTING_TABLE", 58),
    CROPS("CROPS", 59),//Unobtainable
    FARMLAND("FARMLAND", 60),
    FURNACE("FURNACE", 61),
    LIT_FURNACE("LIT_FURNACE", 62),
    STANDING_SIGN("STANDING_SIGN", 63),//Unobtainable
    WOODEN_DOOR_BLOCK("WOODEN_DOOR_BLOCK", 64),//Unobtainable
    LADDER("LADDER", 65),
    RAIL("RAIL", 66),
    STONE_STAIRS("STONE_STAIRS", 67),
    WALL_SIGN("WALL_SIGN", 68),//Unobtainable
    LEVER("LEVER", 69),
    STONE_PRESSURE_PLATE("STONE_PRESSURE_PLATE", 70),
    IRON_DOOR_BLOCK("IRON_DOOR_BLOCK", 71),//Unobtainable
    WOODEN_PRESSURE_PLATE("WOODEN_PRESSURE_PLATE", 72),
    REDSTONE_ORE("REDSTONE_ORE", 73),
    LIT_REDSTONE_ORE("LIT_REDSTONE_ORE", 74),//Unobtainable
    UNLIT_REDSTONE_TORCH("UNLIT_REDSTONE_TORCH", 75),//Unobtainable
    REDSTONE_TORCH("REDSTONE_TORCH", 76),
    STONE_BUTTON("STONE_BUTTON", 77),
    SNOW_LAYER("SNOW_LAYER", 78),
    ICE("ICE", 79),
    SNOW("SNOW", 80, Arrays.asList("SNOW_BLOCK")),
    CACTUS("CACTUS", 81),
    CLAY("CLAY", 82, Arrays.asList("CLAY_BLOCK")),
    REEDS_BLOCK("REEDS_BLOCK", 83),//Unobtainable
    JUKEBOX("JUKEBOX", 84),
    FENCE("FENCE", 85),
    PUMPKIN("PUMPKIN", 86),
    NETHERRACK("NETHERRACK", 87),
    SOUL_SAND("SOUL_SAND", 88),
    GLOWSTONE("GLOWSTONE", 89),
    PORTAL("PORTAL", 90),//Unobtainable
    LIT_PUMPKIN("LIT_PUMPKIN", 91),
    CAKE_BLOCK("CAKE_BLOCK", 92),//Unobtainable
    UNPOWERED_REPEATER("UNPOWERED_REPEATER", 93),//Unobtainable
    POWERED_REPEATER("POWERED_REPEATER", 94),//Unobtainable
    STAINED_GLASS("STAINED_GLASS", 95),
    TRAPDOOR("TRAPDOOR", 96),
    MONSTER_EGG("MONSTER_EGG", 97),
    STONEBRICK("STONEBRICK", 98),
    BROWN_MUSHROOM_BLOCK("BROWN_MUSHROOM_BLOCK", 99),
    RED_MUSHROOM_BLOCK("RED_MUSHROOM_BLOCK", 100),
    IRON_BARS("IRON_BARS", 101),
    GLASS_PANE("GLASS_PANE", 102),
    MELON_BLOCK("MELON_BLOCK", 103),
    PUMPKIN_STEM("PUMPKIN_STEM", 104),//Unobtainable
    MELON_STEM("MELON_STEM", 105),//Unobtainable
    VINE("VINE", 106),
    FENCE_GATE("FENCE_GATE", 107),
    BRICK_STAIRS("BRICK_STAIRS", 108),
    STONE_BRICK_STAIRS("STONE_BRICK_STAIRS", 109),
    MYCELIUM("MYCELIUM", 110),
    WATERLILY("WATERLILY", 111),
    NETHER_BRICK("NETHER_BRICK", 112),
    NETHER_BRICK_FENCE("NETHER_BRICK_FENCE", 113),
    NETHER_BRICK_STAIRS("NETHER_BRICK_STAIRS", 114),
    NETHER_WART_STALK("NETHER_WART_STALK", 115),//Unobtainable
    ENCHANTING_TABLE("ENCHANTING_TABLE", 116),
    BREWING_STAND_BLOCK("BREWING_STAND_BLOCK", 117),//Unobtainable
    CAULDRON_BLOCK("CAULDRON_BLOCK", 118),//Unobtainable
    END_PORTAL("END_PORTAL", 119),//Unobtainable
    END_PORTAL_FRAME("END_PORTAL_FRAME", 120),
    END_STONE("END_STONE", 121),
    DRAGON_EGG("DRAGON_EGG", 122),
    REDSTONE_LAMP("REDSTONE_LAMP", 123),
    LIT_REDSTONE_LAMP("LIT_REDSTONE_LAMP", 124),//Unobtainable
    DOUBLE_WOODEN_SLAB("DOUBLE_WOODEN_SLAB", 125),//Unobtainable
    WOODEN_SLAB("WOODEN_SLAB", 126),
    COCOA_BLOCK("COCOA_BLOCK", 127),//Unobtainable
    SANDSTONE_STAIRS("SANDSTONE_STAIRS", 128),
    EMERALD_ORE("EMERALD_ORE", 129),
    ENDER_CHEST("ENDER_CHEST", 130),
    TRIPWIRE_HOOK("TRIPWIRE_HOOK", 131),
    TRIPWIRE("TRIPWIRE", 132),//Unobtainable
    EMERALD_BLOCK("EMERALD_BLOCK", 133),
    SPRUCE_STAIRS("SPRUCE_STAIRS", 134),
    BIRCH_STAIRS("BIRCH_STAIRS", 135),
    JUNGLE_STAIRS("JUNGLE_STAIRS", 136),
    COMMAND_BLOCK("COMMAND_BLOCK", 137),
    BEACON("BEACON", 138),
    COBBLESTONE_WALL("COBBLESTONE_WALL", 139),
    FLOWER_POT_BLOCK("FLOWER_POT_BLOCK", 140),
    CARROTS_BLOCK("CARROTS_BLOCK", 141),
    POTATOES_BLOCK("POTATOES_BLOCK", 142),
    WOODEN_BUTTON("WOODEN_BUTTON", 143),
    SKULL_BLOCK("SKULL_BLOCK", 144),//Unobtainable
    ANVIL("ANVIL", 145),
    TRAPPED_CHEST("TRAPPED_CHEST", 146),
    LIGHT_WEIGHTED_PRESSURE_PLATE("LIGHT_WEIGHTED_PRESSURE_PLATE", 147),
    HEAVY_WEIGHTED_PRESSURE_PLATE("HEAVY_WEIGHTED_PRESSURE_PLATE", 148),
    UNPOWERED_COMPARATOR("UNPOWERED_COMPARATOR", 149),//Unobtainable
    POWERED_COMPARATOR("POWERED_COMPARATOR", 150),//Unobtainable
    DAYLIGHT_DETECTOR("DAYLIGHT_DETECTOR", 151),
    REDSTONE_BLOCK("REDSTONE_BLOCK", 152),
    QUARTZ_ORE("QUARTZ_ORE", 153),
    HOPPER("HOPPER", 154),
    QUARTZ_BLOCK("QUARTZ_BLOCK", 155),
    QUARTZ_STAIRS("QUARTZ_STAIRS", 156),
    ACTIVATOR_RAIL("ACTIVATOR_RAIL", 157),
    DROPPER("DROPPER", 158),
    STAINED_HARDENED_CLAY("STAINED_HARDENED_CLAY", 159),
    STAINED_GLASS_PANE("STAINED_GLASS_PANE", 160),
    LEAVES2("LEAVES2", 161),
    LOG2("LOG2", 162),
    ACACIA_STAIRS("ACACIA_STAIRS", 163),
    DARK_OAK_STAIRS("DARK_OAK_STAIRS", 164),
    SLIME("SLIME", 165, Arrays.asList("SLIME_BLOCK")),
    BARRIER("BARRIER", 165),
    IRON_TRAPDOOR("IRON_TRAPDOOR", 167),
    PRISMARINE("PRISMARINE", 168),
    SEA_LANTERN("SEA_LANTERN", 169),
    HAY_BLOCK("HAY_BLOCK", 170, Arrays.asList("HAY_BALE")),
    CARPET("CARPET", 171),
    HARDENED_CLAY("HARDENED_CLAY", 172),
    COAL_BLOCK("COAL_BLOCK", 173),
    PACKED_ICE("PACKED_ICE", 174),
    DOUBLE_PLANT("DOUBLE_PLANT", 175),
    STANDING_BANNER("STANDING_BANNER", 176),//Unobtainable
    WALL_BANNER("WALL_BANNER", 177),//Unobtainable
    DAYLIGHT_DETECTOR_INVERTED("DAYLIGHT_DETECTOR_INVERTED", 178),//Unobtainable
    RED_SANDSTONE("RED_SANDSTONE", 179),
    RED_SANDSTONE_STAIRS("RED_SANDSTONE_STAIRS", 180),
    DOUBLE_STONE_SLAB2("DOUBLE_STONE_SLAB2", 181),//Unobtainable
    STONE_SLAB2("STONE_SLAB2", 182),
    SPRUCE_FENCE_GATE("SPRUCE_FENCE_GATE", 183),
    BIRCH_FENCE_GATE("BIRCH_FENCE_GATE", 184),
    JUNGLE_FENCE_GATE("JUNGLE_FENCE_GATE", 185),
    DARK_OAK_FENCE_GATE("DARK_OAK_FENCE_GATE", 186),
    ACACIA_FENCE_GATE("ACACIA_FENCE_GATE", 187),
    SPRUCE_FENCE("SPRUCE_FENCE", 188),
    BIRCH_FENCE("BIRCH_FENCE", 189),
    JUNGE_FENCE("JUNGE_FENCE", 190),
    DARK_OAK_FENCE("DARK_OAK_FENCE", 191),
    ACACIA_FENCE("ACACIA_FENCE", 192),
    SPRUCE_DOOR_BLOCK("SPRUCE_DOOR_BLOCK", 193),//Unobtainable
    BIRCH_DOOR_BLOCK("BIRCH_DOOR_BLOCK", 194),//Unobtainable
    JUGNLE_DOOR_BLOCK("JUGNLE_DOOR_BLOCK", 195),//Unobtainable
    ACACIA_DOOR_BLOCK("ACACIA_DOOR_BLOCK", 196),//Unobtainable
    DARK_OAK_DOOR_BLOCK("DARK_OAK_DOOR_BLOCK", 197);//Unobtainable
    //TODO: Add item ids as well as fill in rest of info for them

    private static HashMap<Integer,Material> idMap = new HashMap<>();
    private static HashMap<String,Material> nameMap = new HashMap<>();
    private final String name;
    private final int id;
    private final short data;
    private List<String> aliases;

    private Material(String name, int id) {
        this.name = name;
        this.id = id;
        this.data = 0;
        this.aliases = new ArrayList<>();
    }

    private Material(String name, int id, List<String> aliases) {
        this.name = name;
        this.id = id;
        this.data = 0;
        this.aliases = aliases;
    }

    private Material(String name, int id, short data) {
        this.name = name;
        this.id = id;
        this.data = data;
        this.aliases = new ArrayList<>();
    }

    private Material(String name, int id, short data, List<String> aliases) {
        this.name = name;
        this.id = id;
        this.data = data;
        this.aliases = aliases;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public short getData() {
        return this.data;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public boolean isBlock() {
        return this.id < 200;
    }

    public int getLightLevel() {
        if (this.equals(FIRE) || this.equals(GLOWSTONE) || this.equals(LIT_PUMPKIN) || this.equals(LAVA) || this.equals(FLOWING_LAVA) ||
                this.equals(BEACON) || this.equals(END_PORTAL) || this.equals(LIT_REDSTONE_LAMP) || this.equals(SEA_LANTERN))
            return 15;
        else if (this.equals(TORCH))
            return 14;
        else if (this.equals(LIT_FURNACE))
            return 13;
        else if (this.equals(PORTAL))
            return 11;
        else if (this.equals(LIT_REDSTONE_ORE))
            return 9;
        else if (this.equals(REDSTONE_TORCH) || this.equals(ENDER_CHEST))
            return 7;
        else if (this.equals(BROWN_MUSHROOM) || this.equals(BREWING_STAND_BLOCK) || this.equals(DRAGON_EGG) || this.equals(END_PORTAL_FRAME))
            return 1;
        return 0;
    }

    public static Material fromID(int id) {
        Material m = idMap.get(id);
        return m == null ? AIR : m;
    }

    public static Material fromString(String name) {
        Material m = nameMap.get(name);
        return m == null ? AIR : m;
    }

    public static void mapMaterials() {
        for(Material m : values()) {
            idMap.put(m.getID(), m);
            nameMap.put(m.getName(), m);
        }
    }
}