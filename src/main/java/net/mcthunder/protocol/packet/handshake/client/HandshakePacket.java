package net.mcthunder.protocol.packet.handshake.client;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class HandshakePacket implements Packet {

    private int protocolVersion;
    private String hostname;
    private int port;
    private int intent;

    @SuppressWarnings("unused")
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

    @Override
    public void read(NetInput in) throws IOException {
        this.protocolVersion = in.readVarInt();
        this.hostname = in.readString();
        this.port = in.readUnsignedShort();
        this.intent = in.readVarInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.protocolVersion);
        out.writeString(this.hostname);
        out.writeShort(this.port);
        out.writeVarInt(this.intent);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

}
