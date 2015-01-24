package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.data.game.values.entity.ProjectileData;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class ThrownEyeOfEnder extends Projectile {
    private byte shake = 0;

    public ThrownEyeOfEnder(Location location) {
        super(location);
        this.type = EntityType.THROWN_EYE_OF_ENDER;
    }

    public ThrownEyeOfEnder(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag shake = tag.get("shake");
        if (shake != null)
            this.shake = shake.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.EYE_OF_ENDER, new ProjectileData(getOwnerID()), this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setShake(byte shake) {
        this.shake = shake;
    }

    public byte getShake() {
        return this.shake;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("shake", this.shake));
        return nbt;
    }
}