package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;

public class Wolf extends Tameable {
    private boolean angry, begging;
    private byte collarColor;

    public Wolf(Location location) {
        super(location);
        this.type = EntityType.WOLF;
        this.angry = false;
        this.begging = false;
        this.collarColor = MetadataConstants.ColorFlags.RED;
        this.metadata.setBit(16, 0x02, this.angry);
        this.metadata.setMetadata(18, this.health);
        this.metadata.setMetadata(19, (byte) (this.begging ? 1 : 0));
        this.metadata.setMetadata(20, this.collarColor);
    }

    @Override
    public void ai() {

    }
}