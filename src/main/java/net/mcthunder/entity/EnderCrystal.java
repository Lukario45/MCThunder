package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

public class EnderCrystal extends Entity {
    private int health;

    public EnderCrystal(Location location) {
        super(location);
        this.type = EntityType.ENDER_CRYSTAL;
        this.health = 1;
        this.metadata.setMetadata(8, this.health);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ENDER_CRYSTAL, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }
}