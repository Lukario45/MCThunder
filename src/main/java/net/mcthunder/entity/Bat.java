package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.MobType;
import org.spacehq.mc.protocol.data.game.world.sound.BuiltinSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Bat extends LivingEntity {
    private boolean hanging = false;

    public Bat(Location location) {
        super(location);
        this.type = EntityType.BAT;
        this.maxHealth = 6;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(16, (byte) (this.hanging ? 1 : 0));
    }

    public Bat(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 6;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        ByteTag batFlags = tag.get("BatFlags");//1 hanging, 0 flying
        this.metadata.setMetadata(16, (byte) ((this.hanging = batFlags != null && batFlags.getValue() == (byte) 1) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, null, MobType.BAT, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public BuiltinSound getDeathSound() {
        return BuiltinSound.ENTITY_BAT_DEATH;
    }

    @Override
    public BuiltinSound getHurtSound() {
        return BuiltinSound.ENTITY_BAT_HURT;
    }

    public void setHanging(boolean hanging) {
        this.metadata.setMetadata(16, (byte) ((this.hanging = hanging) ? 1 : 0));
        updateMetadata();
    }

    public boolean isHanging() {
        return this.hanging;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("BatFlags", (byte) (this.hanging ? 1 : 0)));
        return nbt;
    }
}