package net.mcthunder.api;

import net.mcthunder.material.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum Enchantment {
    PROTECTION("PROTECTION", 0, Material.armorList(), 4),
    FIRE_PROTECTION("FIRE_PROTECTION", 1, Material.armorList(), 4),
    FEATHER_FALLING("FEATHER_FALLING", 2, Material.bootsList(), 4),
    BLAST_PROTECTION("BLAST_PROTECTION", 3, Material.armorList(), 4),
    PROJECTILE_PROTECTION("PROJECTILE_PROTECTION", 4, Material.armorList(), 4),
    RESPIRATION("RESPIRATION", 5, Material.helmetsList(), 3),
    AQUA_AFFINITY("AQUA_AFFINITY", 6, Material.helmetsList(), 1),
    THORNS("THORNS", 7, Material.armorList(), 3),
    DEPTH_STRIDER("DEPTH_STRIDER", 8, Material.bootsList(), 3),
    SHARPNESS("SHARPNESS", 16, swordsAxes(), 5),
    SMITE("SMITE", 17, swordsAxes(), 5),
    BANE_OF_ARTHROPODS("BANE_OF_ARTHROPODS", 18, swordsAxes(), 5),
    KNOCKBACK("KNOCKBACK", 19, Material.swordsList(), 2),
    FIRE_ASPECT("FIRE_ASPECT", 20, Material.swordsList(), 2),
    LOOTING("LOOTING", 21, Material.swordsList(), 3),
    EFFICIENCY("EFFICIENCY", 32, picksShovelsAxesShears(), 5),
    SILK_TOUCH("SILK_TOUCH", 33, picksShovelsAxesShears(), 1),
    UNBREAKING("UNBREAKING", 34, Material.unbreakingList(), 3),
    FORTUNE("FORTUNE", 35, picksShovelsAxes(), 3),
    POWER("POWER", 48, Arrays.asList(Material.BOW), 5),
    PUNCH("PUNCH", 49, Arrays.asList(Material.BOW), 2),
    FLAME("FLAME", 50, Arrays.asList(Material.BOW), 1),
    INFINITY("INFINITY", 51, Arrays.asList(Material.BOW), 1),
    LUCK_OF_THE_SEA("LUCK_OF_THE_SEA", 61, Arrays.asList(Material.FISHING_ROD), 3),
    LURE("LURE", 62, Arrays.asList(Material.FISHING_ROD), 3);

    private static HashMap<Integer,Enchantment> idMap = new HashMap<>();
    private static HashMap<String,Enchantment> nameMap = new HashMap<>();
    private String name;
    private int id;
    private List<Material> items;
    private int maxLevel;

    private Enchantment(String name, int id, List<Material> items, int maxLevel) {
        this.name = name;
        this.id = id;
        this.items = items;
        this.maxLevel = maxLevel;
    }

    public int getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public List<Material> getSuportedItems() {
        return this.items;
    }

    public static Enchantment fromID(int id) {
        return idMap.get(id);
    }

    public static Enchantment fromString(String name) {
        return nameMap.get(name);
    }

    public static void mapEnchantments() {
        for(Enchantment e : values()) {
            idMap.put(e.getID(), e);
            nameMap.put(e.getName(), e);
        }
    }

    private static List<Material> swordsAxes() {
        List<Material> r = new ArrayList<>();
        r.addAll(Material.axesList());
        r.addAll(Material.swordsList());
        return r;
    }

    private static List<Material> picksShovelsAxesShears() {
        List<Material> r = new ArrayList<>();
        r.addAll(Material.axesList());
        r.addAll(Material.pickaxeList());
        r.addAll(Material.shovelList());
        r.add(Material.SHEARS);
        return r;
    }

    private static List<Material> picksShovelsAxes() {
        List<Material> r = new ArrayList<>();
        r.addAll(Material.axesList());
        r.addAll(Material.pickaxeList());
        r.addAll(Material.shovelList());
        return r;
    }
}