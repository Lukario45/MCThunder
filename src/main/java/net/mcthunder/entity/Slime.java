package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

import java.util.Random;

public class Slime extends LivingEntity {
    private boolean wasOnGround;
    private byte size;

    public Slime(Location location) {
        super(location);
        this.type = EntityType.SLIME;
        this.wasOnGround = !this.onGround;
        this.metadata.setMetadata(16, this.size = (byte) (new Random().nextInt(4) + 1));
    }

    public Slime(World w, CompoundTag tag) {
        super(w, tag);
        IntTag size = tag.get("Size");
        ByteTag wasOnGround = tag.get("wasOnGround");//1 true, 0 false
        this.wasOnGround = wasOnGround == null ? !this.onGround : wasOnGround.getValue() == (byte) 1;
        this.metadata.setMetadata(16, this.size = (byte) (size == null ? 1 : size.getValue()));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.SLIME, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setSize(byte size) {
        this.metadata.setMetadata(16, this.size = size);
        updateMetadata();
    }

    public byte getSize() {
        return this.size;
    }

    public void setWasOnGround(boolean wasOnGround) {
        this.wasOnGround = wasOnGround;
    }

    public boolean getWasOnGround() {
        return this.wasOnGround;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("Size", this.size));
        nbt.put(new ByteTag("wasOnGround", (byte) (this.wasOnGround ? 1 : 0)));
        return nbt;
    }
}