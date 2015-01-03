package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Ocelot extends Tameable {
    private byte catType;

    public Ocelot(Location location) {
        super(location);
        this.type = EntityType.OCELOT;
        this.metadata.setMetadata(18, this.catType = (byte) 0);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.OCELOT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setCatType(byte catType) {
        this.metadata.setMetadata(18, this.catType = catType);
        updateMetadata();
    }

    public byte getCatType() {
        return this.catType;
    }
}