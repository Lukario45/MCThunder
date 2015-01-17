package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Giant extends Zombie {
    public Giant(Location location) {
        super(location);
        this.type = EntityType.GIANT;
    }

    public Giant(World w, CompoundTag tag) {
        super(w, tag);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.GIANT_ZOMBIE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}