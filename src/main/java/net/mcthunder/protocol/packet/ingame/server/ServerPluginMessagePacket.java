package net.mcthunder.protocol.packet.ingame.server;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ServerPluginMessagePacket implements Packet {

    private String channel;
    private byte data[];

    @SuppressWarnings("unused")
    private ServerPluginMessagePacket() {
    }

    public ServerPluginMessagePacket(String channel, byte data[]) {
        this.channel = channel;
        this.data = data;
    }

    public String getChannel() {
        return this.channel;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.channel = in.readString();
        this.data = in.readBytes(in.readShort());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.channel);
        out.writeShort(this.data.length);
        out.writeBytes(this.data);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
