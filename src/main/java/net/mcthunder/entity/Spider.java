package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Spider extends LivingEntity {
    private boolean climbing;

    public Spider(Location location) {
        super(location);
        this.type = EntityType.SPIDER;
        this.metadata.setMetadata(16, (byte) ((this.climbing = false) ? 1 : 0));
    }

    public Spider(World w, CompoundTag tag) {
        super(w, tag);
        this.metadata.setMetadata(16, (byte) ((this.climbing = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.SPIDER, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setClimbing(boolean climbing) {
        this.metadata.setMetadata(16, (byte) ((this.climbing = climbing) ? 1 : 0));
        updateMetadata();
    }

    public boolean isClimbing() {
        return this.climbing;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}