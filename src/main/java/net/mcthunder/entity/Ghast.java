package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.data.game.values.world.GenericSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Ghast extends LivingEntity {
    private boolean attacking = false;
    private int explosionPower = 0;

    public Ghast(Location location) {
        super(location);
        this.type = EntityType.GHAST;
        this.maxHealth = 10;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(16, (byte) (this.attacking ? 1 : 0));
    }

    public Ghast(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 10;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        IntTag explosionPower = tag.get("ExplosionPower");
        if (explosionPower != null)
            this.explosionPower = explosionPower.getValue();
        this.metadata.setMetadata(16, (byte) (this.attacking ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.GHAST, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public GenericSound getDeathSound() {
        return GenericSound.GHAST_DEATH;
    }

    @Override
    public GenericSound getHurtSound() {
        return GenericSound.GHAST_HURT;
    }

    public void setAttacking(boolean attacking) {
        this.metadata.setMetadata(16, (byte) ((this.attacking = attacking) ? 1 : 0));
        updateMetadata();
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public void setExplosionPower(int power) {
        this.explosionPower = power;
    }

    public int getExplosionPower() {
        return this.explosionPower;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("ExplosionPower", this.explosionPower));
        return nbt;
    }
}