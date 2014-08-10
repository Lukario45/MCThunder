package net.mcthunder.packet;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class HandshakePacket
        implements Packet {
    private int protocolVersion;
    private String hostname;
    private int port;
    private int intent;

    private HandshakePacket() {
    }

    public HandshakePacket(int protocolVersion, String hostname, int port, int nextState) {
        this.protocolVersion = protocolVersion;
        this.hostname = hostname;
        this.port = port;
        this.intent = nextState;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public int getIntent() {
        return this.intent;
    }

    public void read(NetIn in) throws IOException {
        this.protocolVersion = in.readVarInt();
        this.hostname = in.readString();
        this.port = in.readUnsignedShort();
        this.intent = in.readVarInt();
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.protocolVersion);
        out.writeString(this.hostname);
        out.writeShort(this.port);
        out.writeVarInt(this.intent);
    }

    public boolean isPriority() {
        return true;
    }
}