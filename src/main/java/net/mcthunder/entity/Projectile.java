package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;

public abstract class Projectile extends Entity {
    private int ownerID;
    private boolean hasOwner;

    protected Projectile(Location location) {
        super(location);
        this.ownerID = 0;
        this.hasOwner = false;
    }

    protected Projectile(World w, CompoundTag tag) {
        super(w, tag);
        this.ownerID = 0;
        this.hasOwner = false;
        ShortTag xTile = tag.get("xTile");//Is retrieving this needed
        ShortTag yTile = tag.get("yTile");//Is retrieving this needed
        ShortTag zTile = tag.get("zTile");//Is retrieving this needed
        ByteTag inTile = tag.get("inTile");//1 true, 0 false. Is retrieving this needed
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
}