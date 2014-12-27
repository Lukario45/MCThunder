package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.material.Material;

public class ChestMinecart extends Minecart {
    private ChestInventory inv;

    public ChestMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_CHEST;
        this.inv = new ChestInventory(this.customName.equals("") ? "Chest" : this.customName);
        this.blockType = Material.CHEST;
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
    }

    public ChestInventory getInventory() {
        return this.inv;
    }
}