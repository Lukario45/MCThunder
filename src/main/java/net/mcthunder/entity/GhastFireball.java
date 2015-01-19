package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.Vector;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;

public class GhastFireball extends Projectile {
    private int explosionPower;

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
            this.location.setVector(new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
        }
        IntTag explosionPower = tag.get("ExplosionPower");
        if (explosionPower != null)
            this.explosionPower = explosionPower.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.GHAST_FIREBALL, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setExplosionPower(int power) {
        this.explosionPower = power;
    }

    public int getExplosionPower() {
        return this.explosionPower;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ListTag("direction", Arrays.<Tag>asList(new DoubleTag("dX", this.location.getVector().getdX()),
                new DoubleTag("dY", this.location.getVector().getdY()), new DoubleTag("dZ", this.location.getVector().getdZ()))));
        nbt.put(new IntTag("ExplosionPower", this.explosionPower));
        return nbt;
    }
}