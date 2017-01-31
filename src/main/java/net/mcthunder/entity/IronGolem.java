package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.MobType;
import org.spacehq.mc.protocol.data.game.world.sound.BuiltinSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class IronGolem extends LivingEntity {
    private boolean playerCreated = false;

    public IronGolem(Location location) {
        super(location);
        this.type = EntityType.IRON_GOLEM;
        this.maxHealth = 100;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(16, (byte) (this.playerCreated ? 1 : 0));
    }

    public IronGolem(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 100;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        ByteTag playerCreated = tag.get("PlayerCreated");//1 true, 0 false
        this.metadata.setMetadata(16, (byte) ((this.playerCreated = playerCreated != null && playerCreated.getValue() == (byte) 1) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID,null, MobType.IRON_GOLEM, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public BuiltinSound getDeathSound() {
        return BuiltinSound.ENTITY_IRONGOLEM_DEATH;
    }

    @Override
    public BuiltinSound getHurtSound() {
        return BuiltinSound.ENTITY_IRONGOLEM_HURT;
    }

    public void setPlayerCreated(boolean playerCreated) {
        this.metadata.setMetadata(16, (byte) ((this.playerCreated = playerCreated) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPlayerCreated() {
        return this.playerCreated;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("PlayerCreated", (byte) (this.playerCreated ? 1 : 0)));
        return nbt;
    }
}