package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.Random;

public class Slime extends LivingEntity {
    private byte size;

    public Slime(Location location) {
        super(location);
        this.type = EntityType.SLIME;
        this.metadata.setMetadata(16, this.size = (byte) (new Random().nextInt(4) + 1));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.SLIME, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setSize(byte size) {
        this.metadata.setMetadata(16, this.size = size);
        updateMetadata();
    }

    public byte getSize() {
        return this.size;
    }
}