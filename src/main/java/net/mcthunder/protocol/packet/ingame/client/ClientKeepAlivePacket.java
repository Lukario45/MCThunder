package net.mcthunder.protocol.packet.ingame.client;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ClientKeepAlivePacket implements Packet {

    private int id;

    @SuppressWarnings("unused")
    private ClientKeepAlivePacket() {
    }

    public ClientKeepAlivePacket(int id) {
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