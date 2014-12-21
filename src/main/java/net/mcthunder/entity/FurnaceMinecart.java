package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class FurnaceMinecart extends Minecart {
    private boolean isPowered;

    public FurnaceMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_FURNACE;
        this.isPowered = false;
        this.block = 61;
        this.metadata.setMetadata(16, this.isPowered);
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
    }
}