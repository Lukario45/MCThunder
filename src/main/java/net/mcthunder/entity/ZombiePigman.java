package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class ZombiePigman extends Zombie {
    public ZombiePigman(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE_PIGMAN;
    }

    @Override
    public void ai() {

    }
}