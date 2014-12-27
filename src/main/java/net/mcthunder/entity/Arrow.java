package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

public class Arrow extends Projectile {
    private boolean isCritical;

    public Arrow(Location location) {
        super(location);
        this.type = EntityType.ARROW;
        this.metadata.setMetadata(16, (byte) ((this.isCritical = false) ? 1 : 0));
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ARROW, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setCritical(boolean isCritical) {
        this.metadata.setMetadata(16, (byte) ((this.isCritical = isCritical) ? 1 : 0));
        updateMetadata();
    }

    public boolean isCritical() {
        return this.isCritical;
    }
}