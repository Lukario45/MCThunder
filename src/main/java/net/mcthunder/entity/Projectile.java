package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;

public abstract class Projectile extends Entity {
    private boolean hasOwner = false;
    private int ownerID = 0;

    protected Projectile(Location location) {
        super(location);
    }

    protected Projectile(World w, CompoundTag tag) {
        super(w, tag);
        ShortTag xTile = tag.get("xTile");//Is retrieving this needed
        ShortTag yTile = tag.get("yTile");//Is retrieving this needed
        ShortTag zTile = tag.get("zTile");//Is retrieving this needed
        ByteTag inTile = tag.get("inTile");
    }

    public int getOwnerID() {
        return this.ownerID;
    }

    public void setOwnerID(int id) {
        this.ownerID = id;
    }

    public boolean getHasOwner() {
        return this.hasOwner;
    }

    public void setHasOwner(boolean hasOwner) {
        this.hasOwner = hasOwner;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}