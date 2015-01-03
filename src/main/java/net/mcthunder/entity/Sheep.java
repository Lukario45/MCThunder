package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Sheep extends Ageable {
    private boolean sheared;
    private byte color;

    public Sheep(Location location) {
        super(location);
        this.type = EntityType.SHEEP;
        this.metadata.setMetadata(16, (byte) (((byte)((this.sheared = false) ? 16 : 0))&240 | (this.color = MetadataConstants.ColorFlags.WHITE)&15));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.SHEEP, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setColor(byte color) {
        this.metadata.setMetadata(16, (byte) (((byte)(this.sheared ? 16 : 0))&240 | (this.color = color)&15));
        updateMetadata();
    }

    public byte getColor() {
        return this.color;
    }

    public void setSheared(boolean sheared) {
        this.metadata.setMetadata(16, (byte) (((byte)((this.sheared = sheared) ? 16 : 0))&240 | this.color&15));
        updateMetadata();
    }

    public boolean getSheared() {
        return this.sheared;
    }
}