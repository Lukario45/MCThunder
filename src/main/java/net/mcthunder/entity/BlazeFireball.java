package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.Vector;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.DoubleTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.packetlib.packet.Packet;

public class BlazeFireball extends Projectile {
    public BlazeFireball(Location location) {
        super(location);
        this.type = EntityType.BLAZE_FIREBALL;
    }

    public BlazeFireball(World w, CompoundTag tag) {
        super(w, tag);
        ListTag direction = tag.get("direction");
        if (direction != null) {
            DoubleTag dX = direction.get(0);
            DoubleTag dY = direction.get(1);
            DoubleTag dZ = direction.get(2);
            this.location.setVector(new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
        }
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.BLAZE_FIREBALL, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}