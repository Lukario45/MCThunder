package net.mcthunder.world.generator;

/**
 * Created by conno_000 on 5/27/2015.
 */
public enum Biome {
    OCEAN("Ocean",(byte)0),
    PLAINS("Plains", (byte)1),
    DESERT("Desert",(byte)2),
    EXTREME_HILLS("Extreme Hills",(byte) 3),
    FOREST("Forest",(byte)4),
    TAIGA("Taiga", (byte)5),
    SWAMPLAND("Swampland",(byte)6),
    RIVER("River",(byte)7),
    HELL("Hell",(byte)8),
    SKY("Sky",(byte)9),
    FROZEN_OCEAN("Frozen Ocean",(byte)10),
    FROZEN_RIVER("Frozen River",(byte)11),
    ICE_PLAINS("Ice Plains",(byte)12),
    ICE_MOUNTAINS("Ice Mountains",(byte)13),
    MUSHROOM_ISLAND("Mushroom Island",(byte)14),
    MUSHROOM_ISLAND_SHORE("Mushroom Island Shore",(byte)15),
    BEACH("Beach",(byte)16),
    DESERT_HILLS("Desert Hills",(byte)17),
    FOREST_HILLS("Forest Hills",(byte)18),
    TAIGA_HILLS("Taiga Hills", (byte)19),
    EXTREME_HILLS_EDGE("Extreme Hills Edge",(byte)20),
    JUNGLE("Jungle",(byte)21),
    JUNGLE_HILLS("Jungle Hills",(byte)22),
    JUNGLE_EDGE("Jungle Edge",(byte)23),
    DEEP_OCEAN("Deep Ocean",(byte)24),
    STONE_BEACH("Stone Beach",(byte)25),
    COLD_BEACH("Cold Beach",(byte)26),
    BIRCH_FOREST("Birch Forest",(byte)27),
    BIRCH_FOREST_HILLS("Birch Forest Hills",(byte)28),
    ROOFED_FOREST("Roofed Forest",(byte)29),
    COLD_TAIGA("Cold Taiga",(byte)30),
    COLD_TAIGA_HILLS("Cold Taiga Hills",(byte)31),
    MEGA_TAIGA("Mega Taiga",(byte)32),
    MEGA_TAIGA_HILLS("Mega Taiga Hills",(byte)33),
    EXTREME_HILLS_PLUS("Extreme Hills+",(byte)34),
    SAVANNA("Savanna",(byte)35),
    SAVANNA_PLATEAU("Savanna Plateau",(byte)36),
    MESA("Mesa",(byte)37),
    MESA_PLATEAU_F("Mesa Plateau F",(byte)38),
    MESA_PLATEAU("Mesa Plateau",(byte)39);

    private final String name;
    private final byte value;


    private Biome(String name, byte value){
        this.name = name;
        this.value = value;

    }

    public String getName(){
        return this.name;
    }
    public byte getValue(){
        return this.value;
    }


}
