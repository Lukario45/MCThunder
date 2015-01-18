package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Ocelot extends Tameable {
    private byte catType;

    public Ocelot(Location location) {
        super(location);
        this.type = EntityType.OCELOT;
        this.metadata.setMetadata(18, this.catType = (byte) 0);
    }

    public Ocelot(World w, CompoundTag tag) {
        super(w, tag);
        IntTag catType = tag.get("CatType");
        this.metadata.setMetadata(18, this.catType = (byte) (catType == null ? 0 : catType.getValue()));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.OCELOT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setCatType(byte catType) {
        this.metadata.setMetadata(18, this.catType = catType);
        updateMetadata();
    }

    public byte getCatType() {
        return this.catType;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}