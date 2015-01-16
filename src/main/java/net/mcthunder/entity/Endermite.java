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
    private boolean playerSpawned;
    private int lifeTime;

    public Endermite(Location location) {
        super(location);
        this.type = EntityType.ENDERMITE;
        this.lifeTime = 0;
        this.playerSpawned = false;
    }

    public Endermite(World w, CompoundTag tag) {
        super(w, tag);
        IntTag lifeTime = tag.get("Lifetime");
        ByteTag playerSpawned = tag.get("PlayerSpawned");
        this.lifeTime = lifeTime == null ? 0 : lifeTime.getValue();
        this.playerSpawned = playerSpawned != null && playerSpawned.getValue() == (byte) 1;
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.ENDERMITE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
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
}