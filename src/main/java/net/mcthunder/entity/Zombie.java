package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Zombie extends LivingEntity {
    private boolean child, villager, converting;

    public Zombie(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE;
        this.metadata.setMetadata(12, (byte) ((this.child = false) ? 1 : 0));
        this.metadata.setMetadata(13, (byte) ((this.villager = false) ? 1 : 0));
        this.metadata.setMetadata(14, (byte) ((this.converting = false) ? 1 : 0));
    }

    @Override
    public void ai() {

    }

    public void setChild(boolean child) {
        this.metadata.setMetadata(12, (byte) ((this.child = child) ? 1 : 0));
        updateMetadata();
    }

    public boolean isChild() {
        return this.child;
    }

    public void setVillager(boolean villager) {
        this.metadata.setMetadata(13, (byte) ((this.villager = villager) ? 1 : 0));
        updateMetadata();
    }

    public boolean isVillager() {
        return this.villager;
    }

    public void setConverting(boolean converting) {
        this.metadata.setMetadata(14, (byte) ((this.converting = converting) ? 1 : 0));
        updateMetadata();
    }

    public boolean isConverting() {
        return this.converting;
    }
}