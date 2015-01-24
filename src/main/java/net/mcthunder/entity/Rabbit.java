package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Rabbit extends Ageable {
    private int moreCarrotTicks = 0, rabbitType = 0;

    public Rabbit(Location location) {
        super(location);
        this.type = EntityType.RABBIT;
        this.maxHealth = 10;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(18, (byte) this.rabbitType);
    }

    public Rabbit(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 10;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        IntTag rabbitType = tag.get("RabbitType");
        IntTag moreCarrotTicks = tag.get("MoreCarrotTicks");
        if (moreCarrotTicks != null)
            this.moreCarrotTicks = moreCarrotTicks.getValue();
        if (rabbitType != null)
            this.rabbitType = rabbitType.getValue();
        this.metadata.setMetadata(18, (byte) this.rabbitType);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.RABBIT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setRabbitType(int rabbitType) {
        this.metadata.setMetadata(18, (byte) (this.rabbitType = rabbitType));
        updateMetadata();
    }

    public int getRabbitType() {
        return this.rabbitType;
    }

    public void setMoreCarrotTicks(int moreCarrotTicks) {
        this.moreCarrotTicks = moreCarrotTicks;
    }

    public int getMoreCarrotTicks() {
        return this.moreCarrotTicks;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("RabbitType", this.rabbitType));
        nbt.put(new IntTag("MoreCarrotTicks", this.moreCarrotTicks));
        return nbt;
    }
}