package net.mcthunder.block;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.Inventory;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.opennbt.tag.builtin.*;

public class Chest {
    private String customName;
    private Inventory inv;
    private Location l;

    public Chest(Location l, ListTag items, String customName) {
        this.customName = customName == null ? "Chest" : customName;
        this.l = l;
        this.inv = new Inventory(27, this.customName);
        if (items != null)
            setItems(items);
    }

    private void setItems(ListTag items) {
        for (int i = 0; i < items.size(); i++) {
            CompoundTag item = items.get(i);
            if (item != null) {
                ByteTag amount = item.get("Count");
                ByteTag slot = item.get("Slot");
                ShortTag data = item.get("Damage");
                int id;
                try {
                    StringTag itemID = item.get("id");
                    id = Integer.parseInt(itemID.getValue());
                } catch (Exception e) {//pre 1.8 loading should be ShortTag instead
                    ShortTag itemID = item.get("id");
                    id = itemID.getValue();
                }
                CompoundTag info = item.get("tag");
                this.inv.setSlot(i, new ItemStack(id, amount.getValue(), data.getValue()));
            }
        }
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public Location getLocation() {
        return this.l;
    }

    public String getName() {
        return this.customName;
    }

    public boolean hasCustomName() {
        return !this.customName.equals("Chest");
    }
}