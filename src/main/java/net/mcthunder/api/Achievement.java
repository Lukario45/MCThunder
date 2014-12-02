package net.mcthunder.api;

public enum Achievement {//http://minecraft.gamepedia.com/Achievement
    TAKING_INVENTORY("TAKING_INVENTORY", "openInventory"),
    GETTING_WOOD("GETTING_WOOD", "mineWood", TAKING_INVENTORY),
    BENCHMARKING("BENCHMARKING", "buildWorkBench", GETTING_WOOD),
    TIME_TO_MINE("TIME_TO_MINE", "buildPickaxe", BENCHMARKING),
    HOT_TOPIC("HOT_TOPIC", "buildFurnace", TIME_TO_MINE),
    AQUIRE_HARDWARE("AQUIRE_HARDWARE", "acquireIron", HOT_TOPIC),
    TIME_TO_FARM("TIME_TO_FARM", "buildHoe", BENCHMARKING),
    BAKE_BREAD("BAKE_BREAD", "makeBread", TIME_TO_FARM),
    THE_LIE("THE_LIE", "bakeCake", TIME_TO_FARM);
    //Continue with those past cake

    private Achievement(String name, String internalID) {
        this(name, internalID, null);
    }

    private Achievement(String name, String internalID, Achievement prior) {

    }
}