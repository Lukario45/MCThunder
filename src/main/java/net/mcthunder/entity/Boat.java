package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

public class Boat extends Entity {
    private int timeSinceHit, forwardDirection;
    private float damageTaken;

    public Boat(Location location) {
        super(location);
        this.type = EntityType.BOAT;
        this.timeSinceHit = 0;
        this.forwardDirection = 0;
        this.damageTaken = 0;
        this.metadata.setMetadata(17, this.timeSinceHit);
        this.metadata.setMetadata(18, this.forwardDirection);
        this.metadata.setMetadata(19, this.damageTaken);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.BOAT, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }
}