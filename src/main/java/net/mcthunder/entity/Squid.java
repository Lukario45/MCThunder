package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Squid extends LivingEntity {
    public Squid(Location location) {
        super(location);
        this.type = EntityType.SQUID;
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.SQUID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }
}