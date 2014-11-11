package net.mcthunder.api;

import java.util.HashMap;

public enum EntityType {
    //Drops
    ITEM("ITEM", 1, "Item"),
    XP_ORB("XP_ORB", 2, "XPOrb"),
    //Immobile
    LEAD_KNOT("LEAD_KNOT", 8, "LeashKnot"),
    PAINTING("PAINTING", 9, "Painting"),
    ITEM_FRAME("ITEM_FRAME", 18, "ItemFrame"),
    ARMOR_STAND("ARMOR_STAND", 30, "ArmorStand"),
    ENDER_CRYSTAL("ENDER_CRYSTAL", 200, "EnderCrystal"),
    //Projectiles
    ARROW("ARROW", 10, "Arrow"),
    SNOWBALL("SNOWBALL", 11, "Snowball"),
    GHAST_FIREBALL("GHAST_FIREBALL", 12, "Fireball"),
    BLAZE_FIREBALL("BLAZE_FIREBALL", 13, "SmallFireball"),
    THROWN_ENDER_PEARL("THROWN_ENDER_PEARL", 14, "ThrownEnderpearl"),
    THROWN_EYE_OF_ENDER("THROWN_EYE_OF_ENDER", 15, "EyeOfEnderSignal"),
    THROWN_SPLASH_POTION("THROWN_SPLASH_POTION", 16, "ThrownPotion"),
    THROWN_EXP_BOTTLE("THROWN_EXP_BOTTLE", 17, "ThrownExpBottle"),
    WITHER_SKULL("WITHER_SKULL", 19, "WitherSkull"),
    FIREWORKS_ROCKET("FIREWORKS_ROCKET", 22, "FireworksRocketEntity"),
    //Blocks
    PRIMED_TNT("PRIMED_TNT", 20, "PrimedTnt"),
    FALLING_SAND("FALLING_SAND", 21, "FallingSand"),
    //Vehicles
    MINECART_COMMAND_BLOCK("MINECART_COMMAND_BLOCK", 40, "MinecartCommandBlock"),
    BOAT("BOAT", 41, "Boat"),
    MINECART("MINECART", 42, "MinecartRideable"),
    MINECART_CHEST("MINECART_CHEST", 43, "MinecartChest"),
    MINECART_FURNACE("MINECART_FURNACE", 44, "MinecartFurnace"),
    MINECART_TNT("MINECART_TNT", 45, "MinecartTNT"),
    MINECART_HOPPER("MINECART_HOPPER", 46, "MinecartHopper"),
    MINECART_SPAWNER("MINECART_SPAWNER", 47, "MinecartSpawner"),
    //Generic
    MOB("MOB", 48, "Mob"),
    MONSTER("MONSTER", 49, "Monster"),
    //Hostile mobs
    CREEPER("CREEPER", 50, "Creeper"),
    SKELETON("SKELETON", 51, "Skeleton"),//Wither and normal
    SPIDER("SPIDER", 52, "Spider"),
    GIANT("GIANT", 53, "Giant"),
    ZOMBIE("ZOMBIE", 54, "Zombie"),//Villager and normal
    SLIME("SLIME", 55, "Slime"),
    GHAST("GHAST", 56, "Ghast"),
    ZOMBIE_PIGMAN("ZOMBIE_PIGMAN", 57, "PigZombie"),
    ENDERMAN("ENDERMAN", 58, "Enderman"),
    CAVESPIDER("CAVESPIDER", 59, "CaveSpider"),
    SILVERFISH("SILVERFISH", 60, "Silverfish"),
    BLAZE("BLAZE", 61, "Blaze"),
    MAGMA_CUBE("MAGMA_CUBE", 62, "LavaSlime"),
    ENDER_DRAGON("ENDER_DRAGON", 63, "EnderDragon"),
    WITHER("WITHER", 64, "WitherBoss"),
    WITCH("WITCH", 66, "Witch"),
    ENDERMITE("ENDERMITE", 67, "Endermite"),
    GUARDIAN("GUARDIAN", 68, "Guardian"),//Elder and normal
    //RABBIT("RABBIT", 101, "Rabbit"),//killer rabbit
    //Passive mobs
    BAT("BAT", 65, "Bat"),
    PIG("PIG", 90, "Pig"),
    SHEEP("SHEEP", 91, "Sheep"),
    COW("COW", 92, "Cow"),
    CHICKEN("CHICKEN", 93, "Chicken"),
    SQUID("SQUID", 94, "Squid"),
    WOLF("WOLF", 95, "Wolf"),
    MOOSHROOM("MOOSHROOM", 96, "MushroomCow"),
    SNOW_GOLEM("SNOW_GOLEM", 97, "SnowMan"),
    OCELOT("OCELOT", 98, "Ozelot"),//Don't ask me why the savegame id is with a z it just is
    IRON_GOLEM("IRON_GOLEM", 99, "VillagerGolem"),
    HORSE("HORSE", 100, "EntityHorse"),
    RABBIT("RABBIT", 101, "Rabbit"),
    //NPCs
    VILLAGER("VILLAGER", 120, "Villager");

    private static HashMap<Integer,EntityType> idMap = new HashMap<>();
    private static HashMap<String,EntityType> nameMap = new HashMap<>();
    private String name;
    private String savegameID;
    private int id;

    private EntityType(String name, int id, String savegameID) {
        this.name = name;
        this.id = id;
        this.savegameID = savegameID;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSavegameID() {
        return this.savegameID;
    }

    public static EntityType fromID(int id) {
        return idMap.get(id);
    }

    public static EntityType fromString(String name) {
        return nameMap.get(name.toUpperCase().replaceAll(" ", "_"));
    }

    public static void mapEntityTypes() {
        for(EntityType e : values()) {
            idMap.put(e.getID(), e);
            nameMap.put(e.getName(), e);
        }
    }
}