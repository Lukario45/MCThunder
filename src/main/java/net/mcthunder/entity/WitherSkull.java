package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.Vector;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.data.game.values.entity.ProjectileData;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.DoubleTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;

public class WitherSkull extends Projectile {
    public WitherSkull(Location location) {
        super(location);
        this.type = EntityType.WITHER_SKULL;
    }

    public WitherSkull(World w, CompoundTag tag) {
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
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.WITHER_HEAD_PROJECTILE, new ProjectileData(getOwnerID()), this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ListTag("direction", Arrays.<Tag>asList(new DoubleTag("dX", this.location.getVector().getdX()),
                new DoubleTag("dY", this.location.getVector().getdY()), new DoubleTag("dZ", this.location.getVector().getdZ()))));
        return nbt;
    }
}