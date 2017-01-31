package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.MobType;
import org.spacehq.mc.protocol.data.game.world.sound.BuiltinSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Spider extends LivingEntity {
    private boolean climbing = false;

    public Spider(Location location) {
        super(location);
        this.type = EntityType.SPIDER;
        this.maxHealth = 16;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(16, (byte) (this.climbing ? 1 : 0));
    }

    public Spider(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 16;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(16, (byte) (this.climbing ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID,null, MobType.SPIDER, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public BuiltinSound getDeathSound() {
        return BuiltinSound.ENTITY_SPIDER_DEATH;
    }

    @Override
    public BuiltinSound getHurtSound() {
        return BuiltinSound.ENTITY_SPIDER_HURT;
    }

    public void setClimbing(boolean climbing) {
        this.metadata.setMetadata(16, (byte) ((this.climbing = climbing) ? 1 : 0));
        updateMetadata();
    }

    public boolean isClimbing() {
        return this.climbing;
    }
}