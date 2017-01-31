package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class EnderCrystal extends Entity {
    private int health = 5;

    public EnderCrystal(Location location) {
        super(location);
        this.type = EntityType.ENDER_CRYSTAL;
        this.metadata.setMetadata(8, this.health);
    }

    public EnderCrystal(World w, CompoundTag tag) {
        super(w, tag);
        this.metadata.setMetadata(8, this.health);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID,null, ObjectType.ENDER_CRYSTAL, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setHealth(int health) {
        this.metadata.setMetadata(8, this.health = health);
        updateMetadata();
    }

    public int getHealth() {
        return this.health;
    }
}