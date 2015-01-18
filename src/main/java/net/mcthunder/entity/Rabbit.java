package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Rabbit extends Ageable {
    private int moreCarrotTicks;
    private byte rabbitType;

    public Rabbit(Location location) {
        super(location);
        this.type = EntityType.RABBIT;
        this.moreCarrotTicks = 0;
        this.metadata.setMetadata(18, this.rabbitType = (byte) 0);
    }

    public Rabbit(World w, CompoundTag tag) {
        super(w, tag);
        IntTag rabbitType = tag.get("RabbitType");
        IntTag moreCarrotTicks = tag.get("MoreCarrotTicks");
        this.moreCarrotTicks = moreCarrotTicks == null ? 0 : moreCarrotTicks.getValue();
        this.metadata.setMetadata(18, this.rabbitType = (byte) (rabbitType == null ? 0 : rabbitType.getValue()));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.RABBIT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setRabbitType(byte rabbitType) {
        this.metadata.setMetadata(18, this.rabbitType = rabbitType);
        updateMetadata();
    }

    public byte getRabbitType() {
        return this.rabbitType;
    }

    public void setMoreCarrotTicks(int moreCarrotTicks) {
        this.moreCarrotTicks = moreCarrotTicks;
    }

    public int getMoreCarrotTicks() {
        return this.moreCarrotTicks;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}