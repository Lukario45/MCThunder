package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.packetlib.packet.Packet;

public class Creeper extends LivingEntity {
    private byte fuse = -1, explosionRadius = 0;
    private boolean powered = false, ignited = false;

    public Creeper(Location location) {
        super(location);
        this.type = EntityType.CREEPER;
        this.metadata.setMetadata(16, this.fuse);
        this.metadata.setMetadata(17, (byte) (this.powered ? 1 : 0));
    }

    public Creeper(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag powered = tag.get("powered");//1 true, 0 false
        ByteTag explosionRadius = tag.get("ExplosionRadius");
        if (explosionRadius != null)
            this.explosionRadius = explosionRadius.getValue();
        ShortTag fuse = tag.get("Fuse");
        ByteTag ignited = tag.get("ignited");//1 true, 0 false
        this.ignited = ignited != null && ignited.getValue() == (byte) 1;
        if (fuse != null)
            this.fuse = (byte)(short) fuse.getValue();
        this.metadata.setMetadata(16, this.fuse);
        this.metadata.setMetadata(17, (byte) ((this.powered = powered != null && powered.getValue() == (byte) 1) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.CREEPER, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setFuse(byte fuse) {
        this.metadata.setMetadata(16, this.fuse = fuse);
        updateMetadata();
    }

    public byte getFuse() {
        return this.fuse;
    }

    public void setPowered(boolean powered) {
        this.metadata.setMetadata(17, (byte) ((this.powered = powered) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setExplosionRadius(byte explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public byte getExplosionRadius() {
        return this.explosionRadius;
    }

    public void setIgnited(boolean ignited) {
        this.ignited = ignited;
    }

    public boolean isIgnited() {
        return this.ignited;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("powered", (byte) (this.powered ? 1 : 0)));
        nbt.put(new ByteTag("ExplosionRadius", this.explosionRadius));
        nbt.put(new ShortTag("Fuse", this.fuse));
        nbt.put(new ByteTag("ignited", (byte) (this.ignited ? 1 : 0)));
        return nbt;
    }
}