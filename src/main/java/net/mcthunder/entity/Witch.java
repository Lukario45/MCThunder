package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Witch extends LivingEntity {
    private boolean aggressive = false;

    public Witch(Location location) {
        super(location);
        this.type = EntityType.WITCH;
        this.maxHealth = 26;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(21, (byte) (this.aggressive ? 1 : 0));
    }

    public Witch(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 26;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(21, (byte) (this.aggressive ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.WITCH, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setAggressive(boolean aggressive) {
        this.metadata.setMetadata(21, (byte) ((this.aggressive = aggressive) ? 1 : 0));
        updateMetadata();
    }

    public boolean isAggressive() {
        return this.aggressive;
    }
}