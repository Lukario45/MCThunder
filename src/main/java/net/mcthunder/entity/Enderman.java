package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;

public class Enderman extends LivingEntity {
    private Material blockType;
    private boolean screaming;

    public Enderman(Location location) {
        super(location);
        this.type = EntityType.ENDERMAN;
        this.blockType = Material.AIR;
        this.metadata.setMetadata(16, this.blockType.getParent().getID().shortValue());
        this.metadata.setMetadata(17, (byte) this.blockType.getData());
        this.metadata.setMetadata(18, (byte) ((this.screaming = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setBlockType(Material type) {
        this.blockType = type;
        this.metadata.setMetadata(16, this.blockType.getParent().getID().shortValue());
        this.metadata.setMetadata(17, (byte) this.blockType.getData());
        updateMetadata();
    }

    public Material getBlockType() {
        return this.blockType;
    }

    public void setScreaming(boolean screaming) {
        this.metadata.setMetadata(18, (byte) ((this.screaming = screaming) ? 1 : 0));
        updateMetadata();
    }

    public boolean isScreaming() {
        return this.screaming;
    }
}