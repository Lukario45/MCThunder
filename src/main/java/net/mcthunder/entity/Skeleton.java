package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Skeleton extends LivingEntity {
    private byte skeletonType;//0 normal, 1 wither

    public Skeleton(Location location) {
        super(location);
        this.type = EntityType.SKELETON;
        this.metadata.setMetadata(13, this.skeletonType = (byte) 0);
    }

    public Skeleton(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag skeletonType = tag.get("SkeletonType");
        this.metadata.setMetadata(13, this.skeletonType = (byte) (skeletonType == null ? 0 : skeletonType.getValue()));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.SKELETON, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setSkeletonType(byte skeletonType) {
        this.metadata.setMetadata(13, this.skeletonType = skeletonType);
        updateMetadata();
    }

    public byte getSkeletonType() {
        return this.skeletonType;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}