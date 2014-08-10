package net.mcthunder.packet.ingame.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientKeepAlivePacket
        implements Packet {
    private int id;

    private ClientKeepAlivePacket() {
    }

    public ClientKeepAlivePacket(int id) {
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