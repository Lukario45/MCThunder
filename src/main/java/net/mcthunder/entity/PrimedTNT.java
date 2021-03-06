package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class PrimedTNT extends Entity {
    private byte fuse = 0;

    public PrimedTNT(Location location) {
        super(location);
        this.type = EntityType.PRIMED_TNT;
    }

    public PrimedTNT(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag fuse = tag.get("Fuse");
        if (fuse != null)
            this.fuse = fuse.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.PRIMED_TNT, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public byte getFuse() {
        return this.fuse;
    }

    public void setFuse(byte fuse) {
        this.fuse = fuse;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("Fuse", this.fuse));
        return nbt;
    }
}