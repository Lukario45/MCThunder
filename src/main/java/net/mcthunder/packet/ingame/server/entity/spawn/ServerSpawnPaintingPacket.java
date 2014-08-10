package net.mcthunder.packet.ingame.server.entity.spawn;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSpawnPaintingPacket
        implements Packet {
    private int entityId;
    private Art art;
    private int x;
    private int y;
    private int z;
    private Direction direction;

    private ServerSpawnPaintingPacket() {
    }

    public ServerSpawnPaintingPacket(int entityId, Art art, int x, int y, int z, Direction direction) {
        this.entityId = entityId;
        this.art = art;
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Art getArt() {
        return this.art;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readVarInt();
        this.art = Art.valueOf(in.readString());
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
        this.direction = Direction.values()[in.readInt()];
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeString(this.art.name());
        out.writeInt(this.x);
        out.writeInt(this.y);
        out.writeInt(this.z);
        out.writeInt(this.direction.ordinal());
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Art {
        Kebab,
        Aztec,
        Alban,
        Aztec2,
        Bomb,
        Plant,
        Wasteland,
        Pool,
        Courbet,
        Sea,
        Sunset,
        Creebet,
        Wanderer,
        Graham,
        Match,
        Bust,
        Stage,
        Void,
        SkullAndRoses,
        Wither,
        Fighters,
        Pointer,
        Pigscene,
        BurningSkull,
        Skeleton,
        DonkeyKong;
    }

    public static enum Direction {
        BOTTOM,
        TOP,
        EAST,
        WEST,
        NORTH,
        SOUTH;
    }
}