package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Pig extends Ageable {
    private boolean hasSaddle;

    public Pig(Location location) {
        super(location);
        this.type = EntityType.PIG;
        this.metadata.setMetadata(16, (byte) ((this.hasSaddle = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.PIG, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setHasSaddle(boolean hasSaddle) {
        this.metadata.setMetadata(16, (byte) ((this.hasSaddle = hasSaddle) ? 1 : 0));
        updateMetadata();
    }

    public boolean hasSaddle() {
        return this.hasSaddle;
    }
}