package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;

public class FurnaceMinecart extends Minecart {
    private boolean isPowered;

    public FurnaceMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_FURNACE;
        this.blockType = Material.FURNACE_MINECART;
        this.metadata.setMetadata(16, (byte) ((this.isPowered = false) ? 1 : 0));
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
    }

    public void setPowered(boolean isPowered) {
        this.metadata.setMetadata(16, (byte) ((this.isPowered = isPowered) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPowered() {
        return this.isPowered;
    }
}