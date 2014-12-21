package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Endermite extends Silverfish {
    public Endermite(Location location) {
        super(location);
        this.type = EntityType.ENDERMITE;
    }

    @Override
    public void ai() {

    }
}