package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.MobType;
import org.spacehq.mc.protocol.data.game.world.sound.BuiltinSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Zombie extends LivingEntity {
    private boolean child = false, villager = false, converting = false, canBreakDoors = false;

    public Zombie(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE;
        this.maxHealth = 20;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(12, (byte) (this.child ? 1 : 0));
        this.metadata.setMetadata(13, (byte) (this.villager ? 1 : 0));
        this.metadata.setMetadata(14, (byte) (this.converting ? 1 : 0));
    }

    public Zombie(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 20;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        ByteTag isVillager = tag.get("IsVillager");//1 true, 0 false
        ByteTag isBaby = tag.get("IsBaby");//1 true, 0 false
        IntTag conversionTime = tag.get("ConversionTime");
        ByteTag canBreakDoors = tag.get("CanBreakDoors");//1 true, 0 false
        this.metadata.setMetadata(12, (byte) ((this.child = isBaby != null && isBaby.getValue() == (byte) 1) ? 1 : 0));
        this.metadata.setMetadata(13, (byte) ((this.villager = isVillager != null && isVillager.getValue() == (byte) 1) ? 1 : 0));
        this.metadata.setMetadata(14, (byte) ((this.converting = conversionTime != null && conversionTime.getValue() != -1) ? 1 : 0));
        this.canBreakDoors = canBreakDoors != null && canBreakDoors.getValue() == (byte) 1;
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID,null, MobType.ZOMBIE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public BuiltinSound getDeathSound() {
        return BuiltinSound.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    public BuiltinSound getHurtSound() {
        return BuiltinSound.ENTITY_ZOMBIE_HURT;
    }

    public void setChild(boolean child) {
        this.metadata.setMetadata(12, (byte) ((this.child = child) ? 1 : 0));
        updateMetadata();
    }

    public boolean isChild() {
        return this.child;
    }

    public void setVillager(boolean villager) {
        this.metadata.setMetadata(13, (byte) ((this.villager = villager) ? 1 : 0));
        updateMetadata();
    }

    public boolean isVillager() {
        return this.villager;
    }

    public void setConverting(boolean converting) {
        this.metadata.setMetadata(14, (byte) ((this.converting = converting) ? 1 : 0));
        updateMetadata();
    }

    public boolean isConverting() {
        return this.converting;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("IsVillager", (byte) (this.villager ? 1 : 0)));
        nbt.put(new ByteTag("IsBaby", (byte) (this.child ? 1 : 0)));
        nbt.put(new IntTag("ConversionTime", this.converting ? 1 : 0));//TODO: Fix
        nbt.put(new ByteTag("CanBreakDoors", (byte) (this.canBreakDoors ? 1 : 0)));
        return nbt;
    }
}