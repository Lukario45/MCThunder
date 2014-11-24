package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.ItemStack;

import java.util.HashMap;

public class Inventory {
    private HashMap<Integer,ItemStack> contents;
    private int size;
    private String name;

    public Inventory(int size, String name){
        this.size = size;
        this.name = name;
        contents = new HashMap<>(this.size);
    }

    public void setSlot(int slot, ItemStack i) {
        contents.put(slot, i == null ? new ItemStack(0) : i);
    }

    public ItemStack getItemAt(int slot) {
        return contents.containsKey(slot) ? contents.get(slot) : new ItemStack(0);
    }

    public void add(ItemStack i) {//TODO

    }
}