package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Rabbit extends Ageable {
    private byte rabbitType;

    public Rabbit(Location location) {
        super(location);
        this.type = EntityType.RABBIT;
        this.metadata.setMetadata(18, this.rabbitType = (byte) 0);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.RABBIT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setRabbitType(byte rabbitType) {
        this.metadata.setMetadata(18, this.rabbitType = rabbitType);
        updateMetadata();
    }

    public byte getRabbitType() {
        return this.rabbitType;
    }
}