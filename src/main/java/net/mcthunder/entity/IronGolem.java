package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class IronGolem extends LivingEntity {
    private boolean playerCreated;

    public IronGolem(Location location) {
        super(location);
        this.type = EntityType.IRON_GOLEM;
        this.metadata.setMetadata(16, (byte) ((this.playerCreated = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.IRON_GOLEM, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setPlayerCreated(boolean playerCreated) {
        this.metadata.setMetadata(16, (byte) ((this.playerCreated = playerCreated) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPlayerCreated() {
        return this.playerCreated;
    }
}