package net.mcthunder.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerKeepAlivePacket implements Packet {

    private int id;

    @SuppressWarnings("unused")
    private ServerKeepAlivePacket() {
    }

    public ServerKeepAlivePacket(int id) {
        this.id = id;
    }

    public int getPingId() {
        return this.id;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.id = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.id);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
