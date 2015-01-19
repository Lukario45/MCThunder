package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Pig extends Ageable {
    private boolean hasSaddle = false;

    public Pig(Location location) {
        super(location);
        this.type = EntityType.PIG;
        this.metadata.setMetadata(16, (byte) (this.hasSaddle ? 1 : 0));
    }

    public Pig(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag saddle = tag.get("Saddle");//1 true, 0 false
        this.metadata.setMetadata(16, (byte) ((this.hasSaddle = saddle != null && saddle.getValue() == (byte) 1) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.PIG, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setHasSaddle(boolean hasSaddle) {
        this.metadata.setMetadata(16, (byte) ((this.hasSaddle = hasSaddle) ? 1 : 0));
        updateMetadata();
    }

    public boolean hasSaddle() {
        return this.hasSaddle;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("Saddle", (byte) (this.hasSaddle ? 1 : 0)));
        return nbt;
    }
}