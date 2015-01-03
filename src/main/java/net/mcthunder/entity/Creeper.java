package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Creeper extends LivingEntity {
    private byte fuse;
    private boolean powered;

    public Creeper(Location location) {
        super(location);
        this.type = EntityType.CREEPER;
        this.metadata.setMetadata(16, this.fuse = (byte) -1);
        this.metadata.setMetadata(17, (byte) ((this.powered = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.CREEPER, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setFuse(byte fuse) {
        this.metadata.setMetadata(16, this.fuse = fuse);
        updateMetadata();
    }

    public byte getFuse() {
        return this.fuse;
    }

    public void setPowered(boolean powered) {
        this.metadata.setMetadata(17, (byte) ((this.powered = powered) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPowered() {
        return this.powered;
    }
}