package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.block.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MinecartType;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class TNTMinecart extends Minecart {
    private int fuse = 0;

    public TNTMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_TNT;
        this.blockType = Material.TNT;
        this.metadata.setMetadata(20, 46);
    }

    public TNTMinecart(World w, CompoundTag tag) {
        super(w, tag);
        IntTag fuse = tag.get("TNTFuse");
        if (fuse != null)
            this.fuse = fuse.getValue();
        this.blockType = Material.TNT;
        this.metadata.setMetadata(20, 46);
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.MINECART, MinecartType.TNT, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TNTFuse", this.fuse));
        return nbt;
    }
}