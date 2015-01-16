package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class ThrownEyeOfEnder extends Projectile {
    private byte shake;

    public ThrownEyeOfEnder(Location location) {
        super(location);
        this.type = EntityType.THROWN_EYE_OF_ENDER;
        this.shake = 0;
    }

    public ThrownEyeOfEnder(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag shake = tag.get("shake");
        this.shake = shake == null ? 0 : shake.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.EYE_OF_ENDER, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setShake(byte shake) {
        this.shake = shake;
    }

    public byte getShake() {
        return this.shake;
    }
}