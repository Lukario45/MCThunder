package net.mcthunder.entity;

import net.mcthunder.api.Location;

import java.util.Random;

public class Slime extends LivingEntity {
    private byte size;

    public Slime(Location location) {
        super(location);
        this.type = EntityType.SLIME;
        this.metadata.setMetadata(16, this.size = (byte) (new Random().nextInt(4) + 1));
    }

    @Override
    public void ai() {

    }

    public void setSize(byte size) {
        this.metadata.setMetadata(16, this.size = size);
        updateMetadata();
    }

    public byte getSize() {
        return this.size;
    }
}