package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Enderman extends LivingEntity {
    private short block;
    private byte blockData;
    private boolean screaming;

    public Enderman(Location location) {
        super(location);
        this.type = EntityType.ENDERMAN;
        this.block = 0;
        this.blockData = (byte) 0;
        this.screaming = false;
        this.metadata.setMetadata(16, this.block);
        this.metadata.setMetadata(17, this.blockData);
        this.metadata.setMetadata(18, (byte) (this.screaming ? 1 : 0));
    }

    @Override
    public void ai() {

    }
}