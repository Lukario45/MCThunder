package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Endermite extends Silverfish {
    private boolean playerSpawned = false;
    private int lifeTime = 0;

    public Endermite(Location location) {
        super(location);
        this.type = EntityType.ENDERMITE;
        this.maxHealth = 8;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
    }

    public Endermite(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 8;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        IntTag lifeTime = tag.get("Lifetime");
        ByteTag playerSpawned = tag.get("PlayerSpawned");
        if (lifeTime != null)
            this.lifeTime = lifeTime.getValue();
        this.playerSpawned = playerSpawned != null && playerSpawned.getValue() == (byte) 1;
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.ENDERMITE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public boolean isPlayerSpawned() {
        return this.playerSpawned;
    }

    public void setPlayerSpawned(boolean playerSpawned) {
        this.playerSpawned = playerSpawned;
    }

    public int getLifeTime() {
        return this.lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("Lifetime", this.lifeTime));
        nbt.put(new ByteTag("PlayerSpawned", (byte) (this.playerSpawned ? 1 : 0)));
        return nbt;
    }
}