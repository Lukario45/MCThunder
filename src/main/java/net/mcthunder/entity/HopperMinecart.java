package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.HopperInventory;
import net.mcthunder.material.Material;

public class HopperMinecart extends Minecart {
    private HopperInventory inv;

    public HopperMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_CHEST;
        this.inv = new HopperInventory(this.customName.equals("") ? "Hopper" : this.customName);
        this.blockType = Material.HOPPER;
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
    }

    public HopperInventory getInventory() {
        return this.inv;
    }
}