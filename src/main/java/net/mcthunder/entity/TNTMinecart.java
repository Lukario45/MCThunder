package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;

public class TNTMinecart extends Minecart {
    public TNTMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_TNT;
        this.blockType = Material.TNT;
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
    }
}