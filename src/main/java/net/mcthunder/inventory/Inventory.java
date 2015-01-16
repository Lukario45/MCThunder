package net.mcthunder.inventory;

import net.mcthunder.material.Material;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.opennbt.tag.builtin.*;

import java.util.Collection;
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
        this.id = nextID++;
    }

    public void setSlot(int slot, ItemStack i) {
        this.contents.put(slot, i == null ? new ItemStack(Material.AIR) : i);
    }

    public ItemStack getItemAt(int slot) {
        return this.contents.get(slot) == null ? new ItemStack(Material.AIR) : this.contents.get(slot);
    }

    public void add(ItemStack is) {
        for (int i = 0; i < this.contents.size(); i++)
            if (this.contents.get(i) == null || this.contents.get(i).getType().equals(Material.AIR)) {
                setSlot(i, is);
                break;
            } else if (this.contents.get(i).canStack(is))//TODO: Check with max stack size and stuff also
                this.contents.get(i).setAmount(this.contents.get(i).getAmount() + is.getAmount());
    }

    public Collection<ItemStack> getItems() {
        return this.contents.values();
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

    public void setItems(ListTag items) {
        if (items == null)
            return;
        for (int i = 0; i < items.size(); i++) {
            CompoundTag item = items.get(i);
            if (item != null) {
                ByteTag slot = item.get("Slot");//What is the point of this anyways
                int id;
                try {
                    id = Material.fromString(((StringTag) item.get("id")).getValue().split("minecraft:")[1]).getParent().getID();
                } catch (Exception e) {//pre 1.8 loading should be ShortTag instead
                    id = Integer.parseInt(((ShortTag) item.get("id")).getValue().toString());
                }
                setSlot(i, new ItemStack(Material.fromData(id, ((ShortTag) item.get("Damage")).getValue()),
                        ((ByteTag) item.get("Count")).getValue(), (CompoundTag) item.get("tag")));
            }
        }
    }
}