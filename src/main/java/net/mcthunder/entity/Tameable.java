package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

public abstract class Tameable extends Ageable {
    private boolean sitting, tame;
    private String ownerName;

    protected Tameable(Location location) {
        super(location);
        this.metadata.setBit(16, 0x01, this.sitting = false);
        this.metadata.setBit(16, 0x04, this.tame = false);
        this.metadata.setMetadata(17, this.ownerName = "");
    }

    protected Tameable(World w, CompoundTag tag) {
        super(w, tag);
        this.metadata.setBit(16, 0x01, this.sitting = false);
        this.metadata.setBit(16, 0x04, this.tame = false);
        this.metadata.setMetadata(17, this.ownerName = "");
    }

    public void setSitting(boolean sitting) {
        this.metadata.setBit(16, 0x01, this.sitting = sitting);
        updateMetadata();
    }

    public boolean isSitting() {
        return this.sitting;
    }

    public void setTame(boolean tame) {
        this.metadata.setBit(16, 0x04, this.tame = tame);
        updateMetadata();
    }

    public boolean isTame() {
        return this.tame;
    }

    public void setOwnerName(String ownerName) {
        this.metadata.setMetadata(17, this.ownerName = ownerName);
        updateMetadata();
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}