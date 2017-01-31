package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class LeadKnot extends Entity {
    public LeadKnot(Location location) {
        super(location);
        this.type = EntityType.LEAD_KNOT;
    }

    public LeadKnot(World w, CompoundTag tag) {
        super(w, tag);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID,null, ObjectType.LEASH_KNOT, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}