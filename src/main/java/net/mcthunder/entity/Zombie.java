package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Zombie extends LivingEntity {
    private boolean child, villager, converting, canBreakDoors;

    public Zombie(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE;
        this.metadata.setMetadata(12, (byte) ((this.child = false) ? 1 : 0));
        this.metadata.setMetadata(13, (byte) ((this.villager = false) ? 1 : 0));
        this.metadata.setMetadata(14, (byte) ((this.converting = false) ? 1 : 0));
        this.canBreakDoors = false;
    }

    public Zombie(World w, CompoundTag tag) {
        super(w, tag);
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

    public void setCanBreakDoors(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }
}