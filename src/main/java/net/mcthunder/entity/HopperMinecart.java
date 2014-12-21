package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.HopperInventory;
import net.mcthunder.inventory.Inventory;

public class HopperMinecart extends Minecart {
    private Inventory inv;

    public HopperMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_CHEST;
        this.inv = new HopperInventory(this.customName.equals("") ? "Hopper" : this.customName);
        this.block = 154;
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
    }
}