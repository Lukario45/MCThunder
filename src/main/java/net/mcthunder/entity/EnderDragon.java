package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.MobType;
import org.spacehq.mc.protocol.data.game.world.sound.BuiltinSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class EnderDragon extends LivingEntity {
    public EnderDragon(Location location) {
        super(location);
        this.maxHealth = 200;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.type = EntityType.ENDER_DRAGON;
    }

    public EnderDragon(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 200;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID,null, MobType.ENDER_DRAGON, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public BuiltinSound getDeathSound() {
        return BuiltinSound.ENTITY_ENDERDRAGON_DEATH;
    }

    @Override
    public BuiltinSound getHurtSound() {
        return BuiltinSound.ENTITY_ENDERDRAGON_HURT;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}