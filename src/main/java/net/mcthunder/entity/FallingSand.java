package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

public class FallingSand extends Entity {
    public FallingSand(Location location) {
        super(location);
        this.type = EntityType.FALLING_SAND;
    }

    public FallingSand(World w, CompoundTag tag) {
        super(w, tag);
        IntTag tileID = tag.get("TileID");
        StringTag block = tag.get("Block");
        CompoundTag tileEntityData = tag.get("TileEntityData");
        ByteTag data = tag.get("Data");
        ByteTag time = tag.get("Time");
        ByteTag dropItem = tag.get("DropItem");//1 true, 0 false
        ByteTag hurtEntities = tag.get("HurtEntities");//1 true, 0 false
        IntTag fallHurtMax = tag.get("FallHurtMax");
        FloatTag fallHurtAmount = tag.get("FallHurtAmount");
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.FALLING_BLOCK, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}