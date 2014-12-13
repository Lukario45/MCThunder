package net.mcthunder.inventory;

import net.mcthunder.material.Material;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;

import java.util.HashMap;

public class Inventory {
    private static int nextID = 1;
    protected HashMap<Integer,ItemStack> contents;
    protected final int id;
    protected String name;
    protected int size;
    protected WindowType type;

    public Inventory(int size, String name, WindowType type) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.contents = new HashMap<>(this.size);
        for (int i = 0; i < this.size; i++)
            this.contents.put(i, new ItemStack(Material.AIR));
        this.id = nextID;
        nextID++;
    }

    public void setSlot(int slot, ItemStack i) {
        this.contents.put(slot, i == null ? new ItemStack(Material.AIR) : i);
    }

    public ItemStack getItemAt(int slot) {
        return this.contents.get(slot) == null ? new ItemStack(Material.AIR) : this.contents.get(slot);
    }

    public void add(ItemStack is) {//TODO: Make them stack
        for (int i = 0; i < this.contents.size(); i++)
            if (this.contents.get(i) == null || this.contents.get(i).getType().equals(Material.AIR)) {
                this.contents.put(i, is);
                break;
            }
    }

    public ItemStack[] getItems() {
        ItemStack[] i = new ItemStack[this.size];
        this.contents.values().toArray(i);
        return i;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.id;
    }

    public ServerOpenWindowPacket getView() {
        return new ServerOpenWindowPacket(this.id, this.type, this.name, this.size);
    }
}