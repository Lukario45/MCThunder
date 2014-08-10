package net.mcthunder.packet.ingame.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerKeepAlivePacket
        implements Packet {
    private int id;

    private ServerKeepAlivePacket() {
    }

    public ServerKeepAlivePacket(int id) {
        this.id = id;
    }

    public int getPingId() {
        return this.id;
    }

    public void read(NetIn in) throws IOException {
        this.id = in.readInt();
    }

    public void write(NetOut out) throws IOException, IOException {
        out.writeInt(this.id);
    }

    public boolean isPriority() {
        return false;
    }
}