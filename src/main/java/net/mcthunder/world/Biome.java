package net.mcthunder.world;

import java.util.HashMap;

public enum Biome {
    OCEAN("OCEAN", 0),
    PLAINS("PLAINS", 1),
    DESERT("DESERT", 2),
    EXTREME_HILLS("EXTREME_HILLS", 3),
    FOREST("FOREST", 4),
    TAIGA("TAIGA", 5),
    SWAMPLAND("SWAMPLAND", 6),
    RIVER("RIVER", 7),
    NETHER("NETHER", 8),
    END("END", 9),
    FROZEN_OCEAN("FROZEN_OCEAN", 10),
    FROZEN_RIVER("FROZEN_RIVER", 11),
    ICE_PLAINS("ICE_PLAINS", 12),
    ICE_MOUNTAINS("ICE_MOUNTAINS", 13),
    MUSHROOM_ISLAND("MUSHROOM_ISLAND", 14),
    MUSHROOM_ISLAND_SHORE("MUSHROOM_ISLAND_SHORE", 15),
    BEACH("BEACH", 16),
    DESERT_HILLS("DESERT_HILLS", 17),
    FOREST_HILLS("FOREST_HILLS", 18),
    TAIGA_HILLS("TAIGA_HILLS", 19),
    EXTREME_HILLS_EDGE("EXTREME_HILLS_EDGE", 20),
    JUNGLE("JUNGLE", 21),
    JUNGLE_HILLS("JUNGLE_HILLS", 22),
    JUNGLE_EDGE("JUNGLE_EDGE", 23),
    DEEP_OCEAN("DEEP_OCEAN", 24),
    STONE_BEACH("STONE_BEACH", 25),
    COLD_BEACH("COLD_BEACH", 26),
    BIRCH_FOREST("BIRCH_FOREST", 27),
    BIRCH_FOREST_HILLS("BIRCH_FOREST_HILLS", 28),
    ROOFED_FOREST("ROOFED_FOREST", 29),
    COLD_TAIGA("COLD_TAIGA", 30),
    COLD_TAIGA_HILLS("COLD_TAIGA_HILLS", 31),
    MEGA_TAIGA("MEGA_TAIGA", 32),
    MEGA_TAIGA_HILLS("MEGA_TAIGA_HILLS", 33),
    EXTREME_HILLS_PLUS("EXTREME_HILLS_PLUS", 34),
    SAVANNA("SAVANNA", 35),
    SAVANNA_PLATEAU("SAVANNA_PLATEAU", 36),
    MESA("MESA", 37),
    MESA_PLATEAU_F("MESA_PLATEAU_F", 38),
    MESA_PLATEAU("MESA_PLATEAU", 39),
    SUNFLOWER_PLAINS("SUNFLOWER_PLAINS", 129),
    DESERT_M("DESERT_M", 130),
    EXTREME_HILLS_M("EXTREME_HILLS_M", 131),
    FLOWER_FOREST("FLOWER_FOREST", 132),
    TAIGA_M("TAIGA_M", 133),
    SWAMPLAND_M("SWAMPLAND_M", 134),
    ICE_PLAINS_SPIKES("ICE_PLAINS_SPIKES", 140),
    JUNGLE_M("JUNGLE_M", 149),
    JUNGLE_EDGE_M("JUNGLE_EDGE_M", 151),
    BIRCH_FOREST_M("BIRCH_FOREST_M", 155),
    BIRCH_FOREST_HILLS_M("BIRCH_FOREST_HILLS_M", 156),
    ROOFED_FOREST_M("ROOFED_FOREST_M", 157),
    COLD_TAIGA_M("COLD_TAIGA_M", 158),
    MEGA_SPRUCE_TAIGA("MEGA_SPRUCE_TAIGA", 160),
    MEGA_SPRUCE_TAIGA_HILLS("MEGA_SPRUCE_TAIGA_HILLS", 161),
    EXTREME_HILLS_PLUS_M("EXTREME_HILLS_PLUS_M", 162),
    SAVANNA_M("SAVANNA_M", 163),
    SAVANNA_PLATEAU_M("SAVANNA_PLATEAU_M", 164),
    MESA_BRYCE("MESA_BRYCE", 165),
    MESA_PLATEAU_F_M("MESA_PLATEAU_F_M", 166),
    MESA_PLATEAU_M("MESA_PLATEAU_M", 167),
    UNCALCULATED("UNCALCULATED", -1);

    private static HashMap<Integer,Biome> idMap = new HashMap<>();
    private static HashMap<String,Biome> nameMap = new HashMap<>();
    private String name;
    private int id;

    private Biome(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static Biome fromID(int id) {
        return idMap.get(id);
    }

    public static Biome fromString(String name) {
        return nameMap.get(name.toUpperCase().replaceAll(" ", "_"));
    }

    public static void mapBiomes() {
        for(Biome b : values()) {
            idMap.put(b.getID(), b);
            nameMap.put(b.getName(), b);
        }
    }
}