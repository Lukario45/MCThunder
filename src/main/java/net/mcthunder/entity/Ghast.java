package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Ghast extends LivingEntity {
    private boolean attacking;

    public Ghast(Location location) {
        super(location);
        this.type = EntityType.GHAST;
        this.metadata.setMetadata(16, (byte) ((this.attacking = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.GHAST, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setAttacking(boolean attacking) {
        this.metadata.setMetadata(16, (byte) ((this.attacking = attacking) ? 1 : 0));
        updateMetadata();
    }

    public boolean isAttacking() {
        return this.attacking;
    }
}