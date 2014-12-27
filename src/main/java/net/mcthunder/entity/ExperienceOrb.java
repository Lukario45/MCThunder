package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.spacehq.packetlib.packet.Packet;

public class ExperienceOrb extends Entity {
    private int value;

    protected ExperienceOrb(Location location) {
        super(location);
        this.type = EntityType.XP_ORB;
        this.value = 1;
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnExpOrbPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}