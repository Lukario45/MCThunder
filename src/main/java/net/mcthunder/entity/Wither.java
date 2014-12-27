package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Wither extends LivingEntity {
    private int watched1, watched2, watched3, invulnerableTime;

    public Wither(Location location) {
        super(location);
        this.type = EntityType.WITHER;
        this.metadata.setMetadata(17, this.watched1 = 0);
        this.metadata.setMetadata(18, this.watched2 = 0);
        this.metadata.setMetadata(19, this.watched3 = 0);
        this.metadata.setMetadata(20, this.invulnerableTime = 0);
    }

    @Override
    public void ai() {

    }

    public void setWatched1(int watched1) {
        this.metadata.setMetadata(17, this.watched1 = watched1);
        updateMetadata();
    }

    public int getWatched1() {
        return this.watched1;
    }

    public void setWatched2(int watched2) {
        this.metadata.setMetadata(18, this.watched2 = watched2);
        updateMetadata();
    }

    public int getWatched2() {
        return this.watched2;
    }

    public void setWatched3(int watched3) {
        this.metadata.setMetadata(19, this.watched3 = watched3);
        updateMetadata();
    }

    public int getWatched3() {
        return this.watched3;
    }

    public void setInvulnerableTime(int invulnerableTime) {
        this.metadata.setMetadata(20, this.invulnerableTime = invulnerableTime);
        updateMetadata();
    }

    public int getInvulnerableTime() {
        return this.invulnerableTime;
    }
}