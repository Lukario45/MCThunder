package net.mcthunder.protocol.packet.ingame.client.player;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ClientPlayerDigPacket implements Packet {

    private Status status;
    private int x;
    private int y;
    private int z;
    private Face face;

    @SuppressWarnings("unused")
    private ClientPlayerDigPacket() {
    }

    public ClientPlayerDigPacket(Status status, int x, int y, int z, Face face) {
        this.status = status;
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
    }

    private static Face valueToFace(int value) {
        switch (value) {
            case 0:
                return Face.BOTTOM;
            case 1:
                return Face.TOP;
            case 2:
                return Face.EAST;
            case 3:
                return Face.WEST;
            case 4:
                return Face.NORTH;
            case 5:
                return Face.SOUTH;
            default:
                return Face.INVALID;
        }
    }

    private static int faceToValue(Face face) {
        switch (face) {
            case BOTTOM:
                return 0;
            case TOP:
                return 1;
            case EAST:
                return 2;
            case WEST:
                return 3;
            case NORTH:
                return 4;
            case SOUTH:
                return 5;
            default:
                return 255;
        }
    }

    public Status getStatus() {
        return this.status;
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

    public Face getFace() {
        return this.face;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.status = Status.values()[in.readUnsignedByte()];
        this.x = in.readInt();
        this.y = in.readUnsignedByte();
        this.z = in.readInt();
        this.face = valueToFace(in.readUnsignedByte());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.status.ordinal());
        out.writeInt(this.x);
        out.writeByte(this.y);
        out.writeInt(this.z);
        out.writeByte(faceToValue(this.face));
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    public static enum Status {
        START_DIGGING,
        CANCEL_DIGGING,
        FINISH_DIGGING,
        DROP_ITEM_STACK,
        DROP_ITEM,
        SHOOT_ARROW_OR_FINISH_EATING;
    }

    public static enum Face {
        BOTTOM,
        TOP,
        EAST,
        WEST,
        NORTH,
        SOUTH,
        INVALID;
    }

}
