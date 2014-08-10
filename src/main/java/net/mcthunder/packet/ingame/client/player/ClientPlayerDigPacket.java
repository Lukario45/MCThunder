package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientPlayerDigPacket
        implements Packet {
    private Status status;
    private int x;
    private int y;
    private int z;
    private Face face;

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
        }
        return Face.INVALID;
    }

    private static int faceToValue(Face face) {
        switch (face.ordinal()) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
        }
        return 255;
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

    public void read(NetIn in) throws IOException {
        this.status = Status.values()[in.readUnsignedByte()];
        this.x = in.readInt();
        this.y = in.readUnsignedByte();
        this.z = in.readInt();
        this.face = valueToFace(in.readUnsignedByte());
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.status.ordinal());
        out.writeInt(this.x);
        out.writeByte(this.y);
        out.writeInt(this.z);
        out.writeByte(faceToValue(this.face));
    }

    public boolean isPriority() {
        return false;
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

    public static enum Status {
        START_DIGGING,
        CANCEL_DIGGING,
        FINISH_DIGGING,
        DROP_ITEM_STACK,
        DROP_ITEM,
        SHOOT_ARROW_OR_FINISH_EATING;
    }
}