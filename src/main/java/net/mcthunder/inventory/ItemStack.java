package net.mcthunder.inventory;

import net.mcthunder.api.Enchantment;
import net.mcthunder.material.Material;

import java.util.HashMap;

public class ItemStack {
    private Material type;
    private int amount;
    private HashMap<Enchantment,Integer> enchantments;

    public ItemStack(Material type) {
        this(type, 1);
    }

    public ItemStack(Material type, int amount) {
        this.type = type;
        this.amount = amount;
        this.enchantments = new HashMap<>();
    }

    public Material getType() {
        return this.type;
    }

    public void setType(Material newType) {
        this.type = newType;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int newAmount) {
        this.amount = newAmount;
    }

    public void addEnchantment(Enchantment e, int level) {
        this.enchantments.put(e, level);
    }

    public boolean hasEnchantment(Enchantment e) {
        return this.enchantments.containsKey(e);
    }

    public org.spacehq.mc.protocol.data.game.ItemStack getIS() {//TODO also pass enchants with compoundtag
        return new org.spacehq.mc.protocol.data.game.ItemStack(this.type.getParent().getID(), this.amount, this.type.getData());
    }
}