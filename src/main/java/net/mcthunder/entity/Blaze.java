package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Blaze extends LivingEntity {
    public Blaze(Location location) {
        super(location);
        this.type = EntityType.BLAZE;
        this.metadata.setMetadata(16, (byte) (this.fireTicks > 0 ? 1 : 0));
    }

    public Blaze(World w, CompoundTag tag) {
        super(w, tag);
        this.metadata.setMetadata(16, (byte) (this.fireTicks > 0 ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.BLAZE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}