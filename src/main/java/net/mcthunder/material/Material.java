package net.mcthunder.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Material {//http://minecraft.gamepedia.com/Id
    AIR("AIR", 0),
    STONE("STONE", 1),
    GRASS("GRASS", 2, Arrays.asList("GRASS_BLOCK")),
    DIRT("DIRT", 3),
    COARSE_DIRT("COURSE_DIRT", DIRT, (short) 1),
    PODZOL("PODZOL", DIRT, (short) 2),
    COBBLESTONE("COBBLESTONE", 4, Arrays.asList("COBBLE")),
    PLANKS("PLANKS", 5),
    SAPLING("SAPLING", 6),
    BEDROCK("BEDROCK", 7, Arrays.asList("ADMINIUM")),
    FLOWING_WATER("FLOWING_WATER", 8),
    WATER("WATER", 9, Arrays.asList("STILL_WATER")),
    FLOWING_LAVA("FLOWING_LAVA", 10),
    LAVA("LAVA", 11, Arrays.asList("STILL_LAVA")),
    SAND("SAND", 12),
    GRAVEL("GRAVEL", 13),
    GOLD_ORE("GOLD_ORE", 14),
    IRON_ORE("IRON_ORE", 15),
    COAL_ORE("COAL_ORE", 16),
    LOG("LOG", 17, Arrays.asList("BARK")),
    LEAVES("LEAVES", 18),
    SPONGE("SPONGE", 19),
    GLASS("GLASS", 20),
    LAPIS_ORE("LAPIS_ORE", 21, Arrays.asList("LAPIS_LAZULI_ORE")),
    LAPIS_BLOCK("LAPIS_BLOCK", 22, Arrays.asList("LAPIS_LAZULI_BLOCK")),
    DISPENSER("DISPENSER", 23),
    SANDSTONE("SANDSTONE", 24),
    NOTEBLOCK("NOTEBLOCK", 25),
    BED_BLOCK("BED_BLOCK", 26),//Unobtainable
    GOLDEN_RAIL("GOLDEN_RAIL", 27, Arrays.asList("POWERED_RAIL")),
    DETECTOR_RAIL("DETECTOR_RAIL", 28),
    STICKY_PISTON("STICKY_PISTON", 29),
    WEB("WEB", 30, Arrays.asList("COBWEB", "SPIDER_WEB")),
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
    STONE_SLAB("STONE_SLAB", 44, Arrays.asList("SLAB")),
    BRICK_BLOCK("BRICK_BLOCK", 45),
    TNT("TNT", 46),
    BOOKSHELF("BOOKSHELF", 47),
    MOSSY_COBBLESTONE("MOSSY_COBBLESTONE", 48, Arrays.asList("MOSSY_COBBLE")),
    OBSIDIAN("OBSIDIAN", 49),
    TORCH("TORCH", 50),
    FIRE("FIRE", 51),//Unobtainable
    MOB_SPAWNER("MOB_SPAWNER", 52, Arrays.asList("SPAWNER", "MONSTERS_PAWNER")),
    OAK_STAIRS("OAK_STAIRS", 53),
    CHEST("CHEST", 54),
    REDSTONE_WIRE("REDSTONE_WIRE", 55),//Unobtainable
    DIAMOND_ORE("DIAMOND_ORE", 56),
    DIAMOND_BLOCK("DIAMOND_BLOCK", 57, Arrays.asList("BLOCK_OF_DIAMOND")),
    CRAFTING_TABLE("CRAFTING_TABLE", 58, Arrays.asList("WORKBENCH")),
    WHEAT_BLOCK("WHEAT_BLOCK", 59, Arrays.asList("CROPS")),//Unobtainable
    FARMLAND("FARMLAND", 60, Arrays.asList("SOIL", "TILLED_SOIL", "TILLED_GROUND", "TILLED_LAND")),
    FURNACE("FURNACE", 61),
    LIT_FURNACE("LIT_FURNACE", 62, Arrays.asList("BURNING_FURNACE")),
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
    REDSTONE_TORCH("REDSTONE_TORCH", 76, Arrays.asList("LIT_REDSTONE_TORCH")),
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
    CARROT_BLOCK("CARROT_BLOCK", 141),
    POTATO_BLOCK("POTATO_BLOCK", 142),
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
    DARK_OAK_DOOR_BLOCK("DARK_OAK_DOOR_BLOCK", 197),//Unobtainable
    //ITEM IDS
    IRON_SHOVEL("IRON_SHOVEL", 256),
    IRON_PICKAXE("IRON_PICKAXE", 257),
    IRON_AXE("IRON_AXE", 258),
    FLINT_AND_STEEL("FLINT_AND_STEEL", 259),
    APPLE("APPLE", 260),
    BOW("BOW", 261),
    ARROW("ARROW", 262),
    COAL("COAL", 263),
    DIAMOND("DIAMOND", 264),
    IRON_INGOT("IRON_INGOT", 265),
    GOLD_INGOT("GOLD_INGOT", 266),
    IRON_SWORD("IRON_SWORD", 267),
    WOODEN_SWORD("WOODEN_SWORD", 268),
    WOODEN_SHOVEL("WOODEN_SHOVEL", 269),
    WOODEN_PICKAXE("WOODEN_PICKAXE", 270),
    WOODEN_AXE("WOODEN_AXE", 271),
    STONE_SWORD("STONE_SWORD", 272),
    STONE_SHOVEL("STONE_SHOVEL", 273),
    STONE_PICKAXE("STONE_PICKAXE", 274),
    STONE_AXE("STONE_AXE", 275),
    DIAMOND_SWORD("DIAMOND_SWORD", 276),
    DIAMOND_SHOVEL("DIAMOND_SHOVEL", 277),
    DIAMOND_PICKAXE("DIAMOND_PICKAXE", 278),
    DIAMOND_AXE("DIAMOND_AXE", 279),
    STICK("STICK", 280),
    BOWL("BOWL", 281),
    MUSHROOM_STEW("MUSHROOM_STEW", 282),
    GOLDEN_SWORD("GOLDEN_SWORD", 283),
    GOLDEN_SHOVEL("GOLDEN_SHOVEL", 284),
    GOLDEN_PICKAXE("GOLDEN_PICKAXE", 285),
    GOLDEN_AXE("GOLDEN_AXE", 286),
    STRING("STRING", 287),
    FEATHER("FEATHER", 288),
    GUNPOWDER("GUNPOWDER", 289),
    WOODEN_HOE("WOODEN_HOE", 290),
    STONE_HOE("STONE_HOE", 291),
    IRON_HOE("IRON_HOE", 292),
    DIAMOND_HOE("DIAMOND_HOE", 293),
    GOLDEN_HOE("GOLDEN_HOE", 294),
    WHEAT_SEEDS("WHEAT_SEEDS", 295),
    WHEAT("WHEAT", 296),
    BREAD("BREAD", 297),
    LEATHER_HELMET("LEATHER_HELMET", 298),
    LEATHER_CHESTPLATE("LEATHER_CHESTPLATE", 299),
    LEATHER_LEGGINGS("LEATHER_LEGGINGS", 300),
    LEATHER_BOOTS("LEATHER_BOOTS", 301),
    CHAINMAIL_HELMET("CHAINMAIL_HELMET", 302),
    CHAINMAIL_CHESTPLATE("CHAINMAIL_CHESTPLATE", 303),
    CHAINMAIL_LEGGINGS("CHAINMAIL_LEGGINGS", 304),
    CHAINMAIL_BOOTS("CHAINMAIL_BOOTS", 305),
    IRON_HELMET("IRON_HELMET", 306),
    IRON_CHESTPLATE("IRON_CHESTPLATE", 307),
    IRON_LEGGINGS("IRON_LEGGINGS", 308),
    IRON_BOOTS("IRON_BOOTS", 309),
    DIAMOND_HELMET("DIAMOND_HELMET", 310),
    DIAMOND_CHESTPLATE("DIAMOND_CHESTPLATE", 311),
    DIAMOND_LEGGINGS("DIAMOND_LEGGINGS", 312),
    DIAMOND_BOOTS("DIAMOND_BOOTS", 313),
    GOLDEN_HELMET("GOLDEN_HELMET", 314),
    GOLDEN_CHESTPLATE("GOLDEN_CHESTPLATE", 315),
    GOLDEN_LEGGINGS("GOLDEN_LEGGINGS", 316),
    GOLDEN_BOOTS("GOLDEN_BOOTS", 317),
    FLINT("FLINT", 318),
    PORKCHOP("PORKCHOP", 319),
    COOKED_PORKCHOP("COOKED_PORKCHOP", 320),
    PAINTING("PAINTING", 321),
    GOLDEN_APPLE("GOLDEN_APPLE", 322),
    SIGN("SIGN", 323),
    WOODEN_DOOR("WOODEN_DOOR", 324),
    BUCKET("BUCKET", 325),
    WATER_BUCKET("WATER_BUCKET", 326),
    LAVA_BUCKET("LAVA_BUCKET", 327),
    MINECART("MINECART", 328),
    SADDLE("SADDLE", 329),
    IRON_DOOR("IRON_DOOR", 330),
    REDSTONE("REDSTONE", 331),
    SNOWBALL("SNOWBALL", 332),
    BOAT("BOAT", 333),
    LEATHER("LEATHER", 334),
    MILK_BUCKET("MILK_BUCKET", 335),
    BRICK("BRICK", 336),
    CLAY_BALL("CLAY_BALL", 337),
    REEDS("REEDS", 338),
    PAPER("PAPER", 339),
    BOOK("BOOK", 340),
    SLIME_BALL("SLIME_BALL", 341),
    CHEST_MINECART("CHEST_MINECART", 342),
    FURNACE_MINECART("FURNACE_MINECART", 343),
    EGG("EGG", 344),
    COMPASS("COMPASS", 345),
    FISHING_ROD("FISHING_ROD", 346),
    CLOCK("CLOCK", 347),
    GLOWSTONE_DUST("GLOWSTONE_DUST", 348),
    FISH("FISH", 349),
    COOKED_FISH("COOKED_FISH", 350),
    DYE("DYE", 351),
    BONE("BONE", 352),
    SUGAR("SUGAR", 353),
    CAKE("CAKE", 354),
    BED("BED", 355),
    REPEATER("REPEATER", 356),
    COOKIE("COOKIE", 357),
    FILLED_MAP("FILLED_MAP", 358),
    SHEARS("SHEARS", 359),
    MELON("MELON", 360),
    PUMPKIN_SEEDS("PUMPKIN_SEEDS", 361),
    MELON_SEEDS("MELON_SEEDS", 362),
    BEEF("BEEF", 363),
    COOKED_BEEF("COOKED_BEEF", 364),
    CHICKEN("CHICKEN", 365),
    COOKED_CHICKEN("COOKED_CHICKEN", 366),
    ROTTEN_FLESH("ROTTEN_FLESH", 367),
    ENDER_PEARL("ENDER_PEARL", 368),
    BLAZE_ROD("BLAZE_ROD", 369),
    GHAST_TEAR("GHAST_TEAR", 370),
    GOLD_NUGGET("GOLD_NUGGET", 371),
    NETHER_WART("NETHER_WART", 372),
    POTION("POTION", 373),
    GLASS_BOTTLE("GLASS_BOTTLE", 374),
    SPIDER_EYE("SPIDER_EYE", 375),
    FERMENTED_SPIDER_EYE("FERMENTED_SPIDER_EYE", 376),
    BLAZE_POWDER("BLAZE_POWDER", 377),
    MAGMA_CREAM("MAGMA_CREAM", 378),
    BREWING_STAND("BREWING_STAND", 379),
    CAULDRON("CAULDRON", 380),
    ENDER_EYE("ENDER_EYE", 381),
    SPECKLED_MELON("SPECKLED_MELON", 382),
    SPAWN_EGG("SPAWN_EGG", 383),
    EXPERIENCE_BOTTLE("EXPERIENCE_BOTTLE", 384),
    FIRE_CHARGE("FIRE_CHARGE", 385),
    WRITABLE_BOOK("WRITABLE_BOOK", 386),
    WRITTEN_BOOK("WRITTEN_BOOK", 387),
    EMERALD("EMERALD", 388),
    ITEM_FRAME("ITEM_FRAME", 389),
    FLOWER_POT("FLOWER_POT", 390),
    CARROT("CARROT", 391),
    POTATO("POTATO", 392),
    BAKED_POTATO("BAKED_POTATO", 393),
    POISONOUS_POTATO("POISONOUS_POTATO", 394),
    MAP("MAP", 395),
    GOLDEN_CARROT("GOLDEN_CARROT", 396),
    SKULL("SKULL", 397),
    CARROT_ON_A_STICK("CARROT_ON_A_STICK", 398),
    NETHER_STAR("NETHER_STAR", 399),
    PUMPKIN_PIE("PUMPKIN_PIE", 400),
    FIREWORKS("FIREWORKS", 401),
    FIREWORK_CHARGE("FIREWORK_CHARGE", 402),
    ENCHANTED_BOOK("ENCHANTED_BOOK", 403),
    COMPARATOR("COMPARATOR", 404),
    NETHERBRICK("NETHERBRICK", 405),
    QUARTZ("QUARTZ", 406),
    TNT_MINECART("TNT_MINECART", 407),
    HOPPER_MINECART("HOPPER_MINECART", 408),
    PRISMARINE_SHARD("PRISMARINE_SHARD", 409),
    PRISMARINE_CRYSTALS("PRISMARINE_CRYSTALS", 410),
    RABBIT("RABBIT", 411),
    COOKED_RABBIT("COOKED_RABBIT", 412),
    RABBIT_STEW("RABBIT_STEW", 413),
    RABBIT_FOOT("RABBIT_FOOT", 414),
    RABBIT_HIDE("RABBIT_HIDE", 415),
    ARMOR_STAND("ARMOR_STAND", 416),
    IRON_HORSE_ARMOR("IRON_HORSE_ARMOR", 417),
    GOLDEN_HORSE_ARMOR("GOLDEN_HORSE_ARMOR", 418),
    DIAMOND_HORSE_ARMOR("DIAMOND_HORSE_ARMOR", 419),
    LEAD("LEAD", 420),
    NAME_TAG("NAME_TAG", 421),
    COMMAND_BLOCK_MINECART("COMMAND_BLOCK_MINECART", 422, Arrays.asList("MINECART_WITH_COMMAND_BLOCK", "COMMAND_IN_MINECART", "MINECART_WITH_COMMAND")),
    MUTTON("MUTTON", 423),
    COOKED_MUTTON("COOKED_MUTTON", 424),
    BANNER("BANNER", 425),
    //426 unused
    SPRUCE_DOOR("SPRUCE_DOOR", 427),
    BIRCH_DOOR("BIRCH_DOOR", 428),
    JUNGLE_DOOR("JUNGLE_DOOR", 429),
    ACACIA_DOOR("ACACIA_DOOR", 430),
    DARK_OAK_DOOR("DARK_OAK_DOOR", 431),
    //Music Discs
    RECORD_13("RECORD_13", 2256, Arrays.asList("13", "MUSIC_DISC_13", "MUSIC_13", "C418_13")),
    RECORD_CAT("RECORD_CAT", 2257, Arrays.asList("CAT", "MUSIC_DISC_CAT", "MUSIC_CAT", "C418_CAT")),
    RECORD_BLOCKS("RECORD_BLOCKS", 2258, Arrays.asList("BLOCKS", "MUSIC_DISC_BLOCKS", "MUSIC_BLOCKS", "C418_BLOCKS")),
    RECORD_CHIRP("RECORD_CHIRP", 2259, Arrays.asList("CHIRP", "MUSIC_DISC_CHIRP", "MUSIC_CHIRP", "C418_CHIRP")),
    RECORD_FAR("RECORD_FAR", 2260, Arrays.asList("FAR", "MUSIC_DISC_FAR", "MUSIC_FAR", "C418_FAR")),
    RECORD_MALL("RECORD_MALL", 2261, Arrays.asList("MALL", "MUSIC_DISC_MALL", "MUSIC_MALL", "C418_MALL")),
    RECORD_MELLOHI("RECORD_MELLOHI", 2262, Arrays.asList("MELLOHI", "MUSIC_DISC_MELLOHI", "MUSIC_MELLOHI", "C418_MELLOHI")),
    RECORD_STAL("RECORD_STAL", 2263, Arrays.asList("STAL", "MUSIC_DISC_STAL", "MUSIC_STAL", "C418_STAL")),
    RECORD_STRAD("RECORD_STRAD", 2264, Arrays.asList("STRAD", "MUSIC_DISC_STRAD", "MUSIC_STRAD", "C418_STRAD")),
    RECORD_WARD("RECORD_WARD", 2265, Arrays.asList("WARD", "MUSIC_DISC_WARD", "MUSIC_WARD", "C418_WARD")),
    RECORD_11("RECORD_11", 2266, Arrays.asList("11", "MUSIC_DISC_11", "MUSIC_11", "C418_11")),
    RECORD_WAIT("RECORD_WAIT", 2267, Arrays.asList("WAIT", "MUSIC_DISC_WAIT", "MUSIC_WAIT", "C418_WAIT"));
    //TODO: Add in aliases for all as well as the data value ones materials

    private static HashMap<Integer,Material> idMap = new HashMap<>();
    private static HashMap<String,Material> nameMap = new HashMap<>();
    private static HashMap<String,String> aliasMap = new HashMap<>();
    private static List<Material> armorList;
    private static List<Material> bootsList;
    private static List<Material> helmetsList;
    private static List<Material> axesList;
    private static List<Material> pickaxeList;
    private static List<Material> shovelList;
    private static List<Material> swordsList;
    private static List<Material> hoeList;
    private final Material parent;
    private final String name;
    private final Integer id;
    private final short data;
    private List<String> aliases;

    private Material(String name, int id) {
        this(name, id, new ArrayList<String>());
    }

    private Material(String name, int id, List<String> aliases) {
        this.name = name;
        this.id = id;
        this.data = 0;
        this.aliases = aliases;
        this.parent = null;
    }

    private Material(String name, Material parent, short data) {
        this(name, parent, data, new ArrayList<String>());
    }

    private Material(String name, Material parent, short data, List<String> aliases) {
        this.name = name;
        this.data = data;
        this.id = null;
        this.aliases = aliases;
        this.parent = parent;
    }

    public Material getParent() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public Integer getID() {
        return this.id;
    }

    public short getData() {
        return this.data;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public boolean isBlock() {
        return this.id < 256;
    }

    public boolean isRecord() {
        return this.id > 2255;
    }

    public boolean isLiquid() {
        return this.equals(WATER) || this.equals(FLOWING_WATER) || this.equals(LAVA) || this.equals(FLOWING_LAVA);
    }

    public boolean isLongGrass() {
        return this.equals(TALLGRASS) || this.equals(DOUBLE_PLANT);
    }

    public int getLightLevel() {
        switch(this) {
            case FIRE: case GLOWSTONE: case LIT_PUMPKIN: case LAVA: case FLOWING_LAVA: case BEACON: case END_PORTAL: case LIT_REDSTONE_LAMP: case SEA_LANTERN:
                return 15;
            case TORCH:
                return 14;
            case LIT_FURNACE:
                return 13;
            case PORTAL:
                return 11;
            case LIT_REDSTONE_ORE:
                return 9;
            case REDSTONE_TORCH: case ENDER_CHEST:
                return 7;
            case BROWN_MUSHROOM: case BREWING_STAND_BLOCK: case DRAGON_EGG: case END_PORTAL_FRAME:
                return 1;
            default:
                return 0;
        }
    }

    public static Material fromID(int id) {
        return idMap.get(id);
    }

    public static Material fromString(String name) {//Tries to get based on alias and then on exact name if no alias
        name = name.toUpperCase().replaceAll(" ", "_");
        String n = aliasMap.get(name);
        return n == null ? nameMap.get(name) : nameMap.get(n);
    }

    public static void mapMaterials() {
        for(Material m : values()) {
            if (m.getID() != null)
                idMap.put(m.getID(), m);
            nameMap.put(m.getName(), m);
            for(String alias : m.getAliases())
                aliasMap.put(alias, m.getName());
        }
        armorList = Arrays.asList(LEATHER_BOOTS, LEATHER_CHESTPLATE, LEATHER_HELMET, LEATHER_LEGGINGS, IRON_BOOTS, IRON_CHESTPLATE, IRON_HELMET, IRON_LEGGINGS, GOLDEN_BOOTS, GOLDEN_CHESTPLATE, GOLDEN_HELMET,
                GOLDEN_LEGGINGS, DIAMOND_BOOTS, DIAMOND_CHESTPLATE, DIAMOND_HELMET, DIAMOND_LEGGINGS, CHAINMAIL_BOOTS, CHAINMAIL_CHESTPLATE, CHAINMAIL_HELMET, CHAINMAIL_LEGGINGS);
        bootsList = Arrays.asList(LEATHER_BOOTS, IRON_BOOTS, GOLDEN_BOOTS, DIAMOND_BOOTS, CHAINMAIL_BOOTS);
        helmetsList = Arrays.asList(LEATHER_HELMET, IRON_HELMET, GOLDEN_HELMET, DIAMOND_HELMET, CHAINMAIL_HELMET);
        axesList = Arrays.asList(WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, DIAMOND_AXE);
        swordsList = Arrays.asList(WOODEN_SWORD, STONE_SWORD, IRON_SWORD, GOLDEN_SWORD, DIAMOND_SWORD);
        pickaxeList = Arrays.asList(WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, DIAMOND_PICKAXE);
        shovelList = Arrays.asList(WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, GOLDEN_SHOVEL, DIAMOND_SHOVEL);
        hoeList = Arrays.asList(WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE);
    }

    public static List<Material> armorList() {
        return armorList;
    }

    public static List<Material> bootsList() {
        return bootsList;
    }

    public static List<Material> helmetsList() {
        return helmetsList;
    }

    public static List<Material> axesList() {
        return axesList;
    }

    public static List<Material> swordsList() {
        return swordsList;
    }

    public static List<Material> pickaxeList() {
        return pickaxeList;
    }

    public static List<Material> shovelList() {
        return shovelList;
    }

    public static List<Material> unbreakingList() {
        List<Material> r = new ArrayList<>();
        r.addAll(armorList);
        r.addAll(axesList);
        r.addAll(swordsList);
        r.addAll(pickaxeList);
        r.addAll(shovelList);
        r.addAll(hoeList);
        r.add(FISHING_ROD);
        r.add(FLINT_AND_STEEL);
        r.add(SHEARS);
        r.add(BOW);
        r.add(CARROT_ON_A_STICK);
        return r;
    }
}