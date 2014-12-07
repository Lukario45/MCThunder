package net.mcthunder.api;

import java.util.HashMap;

public enum Achievement {
    TAKING_INVENTORY("TAKING_INVENTORY", "openInventory"),
    GETTING_WOOD("GETTING_WOOD", "mineWood", TAKING_INVENTORY),
    BENCHMARKING("BENCHMARKING", "buildWorkBench", GETTING_WOOD),
    TIME_TO_MINE("TIME_TO_MINE", "buildPickaxe", BENCHMARKING),
    HOT_TOPIC("HOT_TOPIC", "buildFurnace", TIME_TO_MINE),
    ACQUIRE_HARDWARE("ACQUIRE_HARDWARE", "acquireIron", HOT_TOPIC),
    TIME_TO_FARM("TIME_TO_FARM", "buildHoe", BENCHMARKING),
    BAKE_BREAD("BAKE_BREAD", "makeBread", TIME_TO_FARM),
    THE_LIE("THE_LIE", "bakeCake", TIME_TO_FARM),
    GETTING_AN_UPGRADE("GETTING_AN_UPGRADE", "buildBetterPickaxe", TIME_TO_MINE),
    DELICIOUS_FISH("DELICIOUS_FISH", "cookFish", HOT_TOPIC),
    ON_A_RAIL("ON_A_RAIL", "onARail", ACQUIRE_HARDWARE),
    TIME_TO_STRIKE("TIME_TO_STRIKE", "buildSword", BENCHMARKING),
    MONSTER_HUNTER("MONSTER_HUNTER", "killEnemy", TIME_TO_STRIKE),
    COW_TIPPER("COW_TIPPER", "killCow", TIME_TO_STRIKE),
    WHEN_PIGS_FLY("WHEN_PIGS_FLY", "flyPig", COW_TIPPER),
    SNIPER_DUEL("SNIPER_DUEL", "snipeSkeleton", MONSTER_HUNTER),
    DIAMONDS("DIAMONDS", "diamonds", ACQUIRE_HARDWARE),
    WE_NEED_TO_GO_DEEPER("WE_NEED_TO_GO_DEEPER", "portal", DIAMONDS),
    RETURN_TO_SENDER("RETURN_TO_SENDER", "ghast", WE_NEED_TO_GO_DEEPER),
    INTO_FIRE("INTO_FIRE", "blazeRod", WE_NEED_TO_GO_DEEPER),
    LOCAL_BREWERY("LOCAL_BREWERY", "potion", INTO_FIRE),
    THE_END("THE_END", "theEnd", INTO_FIRE),
    THE_END_2("THE_END_2", "theEnd2", THE_END),
    ENCHANTER("ENCHANTER", "enchantments", DIAMONDS),
    OVERKILL("OVERKILL", "overkill", ENCHANTER),
    LIBRARIAN("LIBRARIAN", "bookcase", ENCHANTER),
    ADVENTURING_TIME("ADVENTURING_TIME", "exploreAllBiomes", THE_END),
    THE_BEGINNING("THE_BEGINNING", "spawnWither", THE_END_2),
    THE_BEGINNING_2("THE_BEGINNING_2", "killWither", THE_BEGINNING),
    BEACONATOR("BEACONATOR", "fullBeacon", THE_BEGINNING_2),
    REPOPULATION("REPOPULATION", "breedCow", COW_TIPPER),
    DIAMONDS_TO_YOU("DIAMONDS_TO_YOU", "diamondsToYou", DIAMONDS),
    OVERPOWERED("OVERPOWERED", "overpowered", GETTING_AN_UPGRADE);

    private static HashMap<String,Achievement> nameMap = new HashMap<>();
    private static HashMap<String,Achievement> internalIDMap = new HashMap<>();
    private String name;
    private String internalID;
    private Achievement prior;

    private Achievement(String name, String internalID) {
        this(name, internalID, null);
    }

    private Achievement(String name, String internalID, Achievement prior) {
        this.name = name;
        this.internalID = internalID;
        this.prior = prior;
    }

    public String getName() {
        return this.name;
    }

    public String getInternalID() {
        return this.internalID;
    }

    public Achievement getPrior() {
        return this.prior;
    }

    public static Achievement fromString(String name) {
        return nameMap.get(name.toUpperCase().replaceAll(" ", "_"));
    }

    public static Achievement fromInternalID(String id) {
        return internalIDMap.get(id.toUpperCase().replaceAll(" ", "_"));
    }

    public static void mapAchievements() {
        for(Achievement a : values()) {
            nameMap.put(a.getName(), a);
            internalIDMap.put(a.getInternalID(), a);
        }
    }
}