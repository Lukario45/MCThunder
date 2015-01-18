package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Ghast extends LivingEntity {
    private boolean attacking;

    public Ghast(Location location) {
        super(location);
        this.type = EntityType.GHAST;
        this.metadata.setMetadata(16, (byte) ((this.attacking = false) ? 1 : 0));
    }

    public Ghast(World w, CompoundTag tag) {
        super(w, tag);
        IntTag explosionPower = tag.get("ExplosionPower");
        this.metadata.setMetadata(16, (byte) ((this.attacking = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.GHAST, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setAttacking(boolean attacking) {
        this.metadata.setMetadata(16, (byte) ((this.attacking = attacking) ? 1 : 0));
        updateMetadata();
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}