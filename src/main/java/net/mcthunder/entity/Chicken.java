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

public class Chicken extends Ageable {
    private boolean chickenJockey = false;
    private int layTime = 0;

    public Chicken(Location location) {
        super(location);
        this.type = EntityType.CHICKEN;
        this.maxHealth = 4;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
    }

    public Chicken(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 4;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        ByteTag isChickenJockey = tag.get("IsChickenJockey");//1 true, 0 false
        IntTag eggLayTime = tag.get("EggLayTime");
        this.chickenJockey = isChickenJockey != null && isChickenJockey.getValue() == (byte) 1;
        if (eggLayTime != null)
            this.layTime = eggLayTime.getValue();
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID,null, MobType.CHICKEN, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public BuiltinSound getDeathSound() {
        return BuiltinSound.ENTITY_CHICKEN_DEATH;
    }

    @Override
    public BuiltinSound getHurtSound() {
        return BuiltinSound.ENTITY_CHICKEN_HURT;
    }

    public boolean isChickenJockey() {
        return this.chickenJockey;
    }

    public void setChickenJockey(boolean chickenJockey) {
        this.chickenJockey = chickenJockey;
    }

    public int getLayTime() {
        return this.layTime;
    }

    public void setLayTime(int layTime) {
        this.layTime = layTime;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("IsChickenJockey", (byte) (this.chickenJockey ? 1 : 0)));
        nbt.put(new IntTag("EggLayTime", this.layTime));
        return nbt;
    }
}