package net.mcthunder.block;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.inventory.Inventory;
import org.spacehq.opennbt.tag.builtin.ListTag;

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
        this.inv.setItems(items);
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