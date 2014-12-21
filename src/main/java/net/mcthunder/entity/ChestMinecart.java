package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.inventory.Inventory;

public class ChestMinecart extends Minecart {
    private Inventory inv;

    public ChestMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_CHEST;
        this.inv = new ChestInventory(this.customName.equals("") ? "Chest" : this.customName);
        this.block = 54;
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
    }
}