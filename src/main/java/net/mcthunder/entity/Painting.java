package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.object.HangingDirection;
import org.spacehq.mc.protocol.data.game.entity.type.PaintingType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class Painting extends Entity {
    private HangingDirection direction = HangingDirection.SOUTH;
    private PaintingType picture = PaintingType.ALBAN;

    public Painting(Location location) {
        super(location);
        this.type = EntityType.PAINTING;
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
            this.picture = PaintingType.valueOf(motive.getValue());
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnPaintingPacket(this.entityID, null, this.picture, this.location.getPosition(), this.direction);
    }

    public void setPicture(PaintingType picture) {
        this.picture = picture;
    }

    public PaintingType getPicture() {
        return this.picture;
    }

    public void setHangingDirection(HangingDirection direction) {
        this.direction = direction;
    }

    public HangingDirection getHangingDirection() {
        return this.direction;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TileX", (int) this.location.getX()));
        nbt.put(new IntTag("TileY", (int) this.location.getY()));
        nbt.put(new IntTag("TileZ", (int) this.location.getZ()));
        nbt.put(new ByteTag("Facing", (byte) (this.direction.equals(HangingDirection.SOUTH) ? 0 : this.direction.equals(HangingDirection.WEST) ? 1 :
                this.direction.equals(HangingDirection.NORTH) ? 2 : 4)));
        nbt.put(new StringTag("Motive", this.picture.name()));
        return nbt;
    }
}