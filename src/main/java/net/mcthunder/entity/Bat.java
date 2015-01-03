package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Bat extends LivingEntity {
    private boolean hanging;

    public Bat(Location location) {
        super(location);
        this.type = EntityType.BAT;
        this.metadata.setMetadata(16, (byte) ((this.hanging = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.BAT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setHanging(boolean hanging) {
        this.metadata.setMetadata(16, (byte) ((this.hanging = hanging) ? 1 : 0));
        updateMetadata();
    }

    public boolean isHanging() {
        return this.hanging;
    }
}