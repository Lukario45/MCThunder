package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Giant extends Zombie {
    public Giant(Location location) {
        super(location);
        this.type = EntityType.GIANT;
        this.maxHealth = 100;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
    }

    public Giant(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = 100;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, null, MobType.GIANT_ZOMBIE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }
}