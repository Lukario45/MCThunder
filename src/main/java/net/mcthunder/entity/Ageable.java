package net.mcthunder.entity;

import net.mcthunder.api.Location;

public abstract class Ageable extends LivingEntity {
    private byte age;

    public Ageable(Location location) {
        super(location);
        this.metadata.setMetadata(12, this.age = (byte) 0);//age in ticks, negative implies child
    }

    public void setAge(byte age) {
        this.metadata.setMetadata(12, this.age = age);
        updateMetadata();
    }

    public byte getAge() {
        return this.age;
    }
}