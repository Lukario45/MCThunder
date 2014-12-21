package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class TNTMinecart extends Minecart {
    public TNTMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_TNT;
        this.block = 46;
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
    }
}