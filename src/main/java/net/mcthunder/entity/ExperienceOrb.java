package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.packetlib.packet.Packet;

public class ExperienceOrb extends Entity {
    private short age, value, health;

    public ExperienceOrb(Location location) {
        super(location);
        this.type = EntityType.XP_ORB;
        this.value = 1;
        this.health = 1;
        this.age = 0;
    }

    public ExperienceOrb(World w, CompoundTag tag) {
        super(w, tag);
        ShortTag age = tag.get("Age");
        ShortTag health = tag.get("Health");
        ShortTag value = tag.get("Value");
        this.value = value == null ? 1 : value.getValue();
        this.health = health == null ? 1 : health.getValue();
        this.age = age == null ? 0 : age.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnExpOrbPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.value);
    }

    public void setValue(short value) {
        this.value = value;
    }

    public short getValue() {
        return this.value;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public short getAge() {
        return this.age;
    }

    public void getHealth(short health) {
        this.health = health;
    }

    public short setHealth() {
        return this.health;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}