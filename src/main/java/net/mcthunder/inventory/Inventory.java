package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.ItemStack;

import java.util.HashMap;

public class Inventory {
    private static int nextID = 1;
    private HashMap<Integer,ItemStack> contents;
    private final int id;
    private String name;

    public Inventory(int size, String name){
        this.name = name;
        this.contents = new HashMap<>(size);
        for (int i = 0; i < this.contents.size(); i++)
            this.contents.put(i, new ItemStack(0));
        this.id = nextID;
        nextID++;
    }

    public void setSlot(int slot, ItemStack i) {
        this.contents.put(slot, i == null ? new ItemStack(0) : i);
    }

    public ItemStack getItemAt(int slot) {
        return this.contents.get(slot) == null ? new ItemStack(0) : this.contents.get(slot);
    }

    public void add(ItemStack is) {//TODO: Make them stack
        for (int i = 0; i < this.contents.size(); i++)
            if (this.contents.get(i) == null || this.contents.get(i).getId() == 0) {
                this.contents.put(i, is);
                break;
            }
    }

    public ItemStack[] getItems() {
        ItemStack[] i = new ItemStack[contents.size()];
        this.contents.values().toArray(i);
        return i;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }
}