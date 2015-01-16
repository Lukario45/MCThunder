package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.Vector;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.DoubleTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.packetlib.packet.Packet;

public class GhastFireball extends Projectile {
    public GhastFireball(Location location) {
        super(location);
        this.type = EntityType.GHAST_FIREBALL;
    }

    public GhastFireball(World w, CompoundTag tag) {
        super(w, tag);
        ListTag direction = tag.get("direction");
        if (direction != null) {
            DoubleTag dX = direction.get(0);
            DoubleTag dY = direction.get(1);
            DoubleTag dZ = direction.get(2);
            this.location.setVector(this.motion = new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
        }
        IntTag explosionPower = tag.get("ExplosionPower");
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.GHAST_FIREBALL, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }
}