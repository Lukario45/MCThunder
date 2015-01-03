package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Zombie extends LivingEntity {
    private boolean child, villager, converting;

    public Zombie(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE;
        this.metadata.setMetadata(12, (byte) ((this.child = false) ? 1 : 0));
        this.metadata.setMetadata(13, (byte) ((this.villager = false) ? 1 : 0));
        this.metadata.setMetadata(14, (byte) ((this.converting = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.ZOMBIE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

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
}