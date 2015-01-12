package net.mcthunder.block;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.inventory.Inventory;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;
import org.spacehq.opennbt.tag.builtin.*;

public class Chest {
    private final Location l;
    private final Block block;
    private String customName;
    private Inventory inv;

    public Chest(Location l, ListTag items, String customName) {
        this.customName = customName == null ? "Chest" : customName;
        this.l = l;
        this.block = new Block(this.l);
        this.inv = new ChestInventory(this.customName);
        if (items != null)
            setItems(items);
    }

    private void setItems(ListTag items) {
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
                this.inv.setSlot(i, new ItemStack(Material.fromData(id, ((ShortTag) item.get("Damage")).getValue()),
                        ((ByteTag) item.get("Count")).getValue(), (CompoundTag) item.get("tag")));
            }
        }
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public Location getLocation() {
        return this.l.clone();
    }

    public String getName() {
        return this.customName;
    }

    public Block getBlock() {
        return block;
    }
}