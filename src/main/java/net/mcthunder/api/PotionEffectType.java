package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.values.entity.Effect;

import java.util.HashMap;

public enum PotionEffectType {
    SPEED("SPEED", 1, Effect.SPEED),
    SLOWNESS("SLOWNESS", 2, Effect.SLOWNESS),
    HASTE("HASTE", 3, Effect.DIG_SPEED),
    MINING_FATIGUE("MINING_FATIGUE", 4, Effect.DIG_SLOWNESS),
    STRENGTH("STRENGTH", 5, Effect.DAMAGE_BOOST),
    INSTANT_HEALTH("INSTANT_HEALTH", 6, Effect.HEAL),
    INSTANT_DAMAGE("INSTANT_DAMAGE", 7, Effect.DAMAGE),
    JUMP_BOOST("JUMP_BOOST", 8, Effect.JUMP_BOOST),
    NAUSEA("NAUSEA", 9, Effect.CONFUSION),
    REGENERATION("REGENERATION", 10, Effect.REGENERATION),
    RESISTANCE("RESISTANCE", 11, Effect.RESISTANCE),
    FIRE_RESISTANCE("FIRE_RESISTANCE", 12, Effect.FIRE_RESISTANCE),
    WATER_BREATHING("WATER_BREATHING", 13, Effect.WATER_BREATHING),
    INVISIBILITY("INVISIBILITY", 14, Effect.INVISIBILITY),
    BLINDNESS("BLINDNESS", 15, Effect.BLINDNESS),
    NIGHT_VISION("NIGHT_VISION", 16, Effect.NIGHT_VISION),
    HUNGER("HUNGER", 17, Effect.HUNGER),
    WEAKNESS("WEAKNESS", 18, Effect.WEAKNESS),
    POISON("POISON", 19, Effect.POISON),
    WITHER("WITHER", 20, Effect.WITHER_EFFECT),
    HEALTH_BOOST("HEALTH_BOOST", 21, Effect.HEALTH_BOOST),
    ABSORPTION("ABSORPTION", 22, Effect.ABSORPTION),
    SATURATION("SATURATION", 23, Effect.SATURATION);

    private static HashMap<Integer,PotionEffectType> idMap = new HashMap<>();
    private static HashMap<String,PotionEffectType> nameMap = new HashMap<>();
    private String name;
    private int id;
    private Effect particle;

    private PotionEffectType(String name, int id, Effect particle) {
        this.name = name;
        this.id = id;
        this.particle = particle;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Effect getParticleEffect() {
        return this.particle;
    }

    public int getColor() {
        switch (this) {
            case SPEED:
                return 0x7cafc6;
            case SLOWNESS:
                return 0x5a6c81;
            case HASTE:
                return 0xd9c043;
            case MINING_FATIGUE:
                return 0x4a4217;
            case STRENGTH:
                return 0x932423;
            case INSTANT_HEALTH:
                return 0xf82423;
            case INSTANT_DAMAGE:
                return 0x430a09;
            case JUMP_BOOST:
                return 0x786297;
            case NAUSEA:
                return 0x551d4a;
            case REGENERATION:
                return 0xcd5cab;
            case RESISTANCE:
                return 0x99453a;
            case FIRE_RESISTANCE:
                return 0xe49a3a;
            case WATER_BREATHING:
                return 0x2e5299;
            case INVISIBILITY:
                return 0x7f8392;
            case BLINDNESS:
                return 0x1f1f23;
            case NIGHT_VISION:
                return 0x1f1fa1;
            case HUNGER:
                return 0x587653;
            case WEAKNESS:
                return 0x484d48;
            case POISON:
                return 0x4e9331;
            case WITHER:
                return 0x352a27;
            case HEALTH_BOOST:
                return 0xf87d23;
            case ABSORPTION:
                return 0x2552a5;
            case SATURATION:
                return 0xf82423;
            default:
                return 0;
        }
    }

    public static PotionEffectType fromID(int id) {
        return idMap.get(id);
    }

    public static PotionEffectType fromString(String name) {
        return nameMap.get(name.toUpperCase().replaceAll(" ", "_"));
    }

    public static void mapPotionEffects() {
        for(PotionEffectType p : values()) {
            idMap.put(p.getID(), p);
            nameMap.put(p.getName(), p);
        }
    }
}