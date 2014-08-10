package net.mcthunder.packet.ingame.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientRequestPacket
        implements Packet {
    private Request request;

    private ClientRequestPacket() {
    }

    public ClientRequestPacket(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return this.request;
    }

    public void read(NetIn in) throws IOException {
        this.request = Request.values()[in.readByte()];
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.request.ordinal());
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Request {
        RESPAWN,
        STATS,
        OPEN_INVENTORY_ACHIEVEMENT;
    }
}