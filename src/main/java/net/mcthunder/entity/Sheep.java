package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;

public class Sheep extends Ageable {
    private boolean sheared;
    private byte color;

    public Sheep(Location location) {
        super(location);
        this.type = EntityType.SHEEP;
        this.color = MetadataConstants.ColorFlags.WHITE;
        this.sheared = false;
        //this.metadata.setBit(16, 0x0F, this.color);
        //this.metadata.setBit(16, 0x10, this.sheared);
        this.metadata.setMetadata(16, ((this.color >>> 4)&0x0F) | (this.sheared ? 1 : 0));//TODO: Check if this works
    }

    @Override
    public void ai() {

    }
}