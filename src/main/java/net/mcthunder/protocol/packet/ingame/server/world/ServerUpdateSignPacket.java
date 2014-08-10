package net.mcthunder.protocol.packet.ingame.server.world;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ServerUpdateSignPacket implements Packet {

    private int x;
    private int y;
    private int z;
    private String lines[];

    @SuppressWarnings("unused")
    private ServerUpdateSignPacket() {
    }

    public ServerUpdateSignPacket(int x, int y, int z, String lines[]) {
        if (lines.length != 4) {
            throw new IllegalArgumentException("Lines must contain exactly 4 strings!");
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.lines = lines;
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

    public String[] getLines() {
        return this.lines;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.x = in.readInt();
        this.y = in.readShort();
        this.z = in.readInt();
        this.lines = new String[4];
        for (int count = 0; count < this.lines.length; count++) {
            this.lines[count] = in.readString();
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.x);
        out.writeShort(this.y);
        out.writeInt(this.z);
        for (String line : this.lines) {
            out.writeString(line);
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
