package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Wither extends LivingEntity {
    private int watched1, watched2, watched3, invulnerableTime;

    public Wither(Location location) {
        super(location);
        this.type = EntityType.WITHER;
        this.watched1 = 0;
        this.watched2 = 0;
        this.watched3 = 0;
        this.invulnerableTime = 0;
        this.metadata.setMetadata(17, this.watched1);
        this.metadata.setMetadata(18, this.watched2);
        this.metadata.setMetadata(19, this.watched3);
        this.metadata.setMetadata(20, this.invulnerableTime);
    }

    @Override
    public void ai() {

    }
}