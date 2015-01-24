package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Boat extends Entity {
    private int timeSinceHit = 0, forwardDirection = 0;
    private float damageTaken = 0;

    public Boat(Location location) {
        super(location);
        this.type = EntityType.BOAT;

        this.metadata.setMetadata(17, this.timeSinceHit);
        this.metadata.setMetadata(18, this.forwardDirection);
        this.metadata.setMetadata(19, this.damageTaken);
    }

    public Boat(World w, CompoundTag tag) {
        super(w, tag);
        this.metadata.setMetadata(17, this.timeSinceHit);
        this.metadata.setMetadata(18, this.forwardDirection);
        this.metadata.setMetadata(19, this.damageTaken);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.BOAT, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setTimeSinceHit(int time) {
        this.metadata.setMetadata(17, this.timeSinceHit = time);
        updateMetadata();
    }

    public int getTimeSinceHit() {
        return this.timeSinceHit;
    }

    public void setForwardDirection(int forwardDirection) {
        this.metadata.setMetadata(18, this.forwardDirection = forwardDirection);
        updateMetadata();
    }

    public int getForwardDirection() {
        return this.forwardDirection;
    }

    public void setDamageTaken(float damageTaken) {
        this.metadata.setMetadata(19, this.damageTaken = damageTaken);
        updateMetadata();
    }

    public float getDamageTaken() {
        return this.damageTaken;
    }
}