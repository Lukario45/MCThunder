package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.Art;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class Painting extends Entity {
    private HangingDirection direction;
    private Art picture;

    public Painting(Location location) {
        super(location);
        this.type = EntityType.PAINTING;
        this.direction = HangingDirection.SOUTH;
        this.picture = Art.ALBAN;
    }

    public Painting(World w, CompoundTag tag) {
        super(w, tag);
        IntTag tileX = tag.get("TileX");//Is retrieving this needed
        IntTag tileY = tag.get("TileY");//Is retrieving this needed
        IntTag tileZ = tag.get("TileZ");//Is retrieving this needed
        ByteTag facing = tag.get("Facing");
        byte temp = facing == null ? 0 : facing.getValue();
        this.direction = temp == 0 ? HangingDirection.SOUTH : temp == (byte) 1 ? HangingDirection.WEST : temp == (byte) 2 ?
                HangingDirection.NORTH : HangingDirection.EAST;
        StringTag motive = tag.get("Motive");
        if (motive != null)
            this.picture = Art.valueOf(motive.getValue());
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnPaintingPacket(this.entityID, this.picture, this.location.getPosition(), this.direction);
    }

    public void setPicture(Art picture) {
        this.picture = picture;
    }

    public Art getPicture() {
        return this.picture;
    }

    public void setHangingDirection(HangingDirection direction) {
        this.direction = direction;
    }

    public HangingDirection getHangingDirection() {
        return this.direction;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}