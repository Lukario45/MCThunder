package net.mcthunder.entity;

import net.mcthunder.api.Location;

public abstract class Projectile extends Entity {
    private int ownerID;
    private boolean hasOwner;

    public Projectile(Location location) {
        super(location);
        this.ownerID = 0;
        this.hasOwner = false;
    }
}