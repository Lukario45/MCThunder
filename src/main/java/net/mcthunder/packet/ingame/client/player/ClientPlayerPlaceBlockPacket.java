package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientPlayerPlaceBlockPacket
        implements Packet {
    private int x;
    private int y;
    private int z;
    private Face face;
    private ItemStack held;
    private float cursorX;
    private float cursorY;
    private float cursorZ;

    private ClientPlayerPlaceBlockPacket() {
    }

    public ClientPlayerPlaceBlockPacket(int x, int y, int z, Face face, ItemStack held, float cursorX, float cursorY, float cursorZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
        this.held = held;
        this.cursorX = cursorX;
        this.cursorY = cursorY;
        this.cursorZ = cursorZ;
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

    public ItemStack getHeldItem() {
        return this.held;
    }

    public float getCursorX() {
        return this.cursorX;
    }

    public float getCursorY() {
        return this.cursorY;
    }

    public float getCursorZ() {
        return this.cursorZ;
    }

    public void read(NetIn in) throws IOException {
        this.x = in.readInt();
        this.y = in.readUnsignedByte();
        this.z = in.readInt();
        int face = in.readUnsignedByte();
        this.face = (face == 255 ? Face.UNKNOWN : Face.values()[face]);
        this.held = NetUtil.readItem(in);
        this.cursorX = (in.readByte() / 16.0F);
        this.cursorY = (in.readByte() / 16.0F);
        this.cursorZ = (in.readByte() / 16.0F);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.x);
        out.writeByte(this.y);
        out.writeInt(this.z);
        out.writeByte(this.face == Face.UNKNOWN ? 255 : this.face.ordinal());
        NetUtil.writeItem(out, this.held);
        out.writeByte((int) (this.cursorX * 16.0F));
        out.writeByte((int) (this.cursorY * 16.0F));
        out.writeByte((int) (this.cursorZ * 16.0F));
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
        UNKNOWN;
    }
}