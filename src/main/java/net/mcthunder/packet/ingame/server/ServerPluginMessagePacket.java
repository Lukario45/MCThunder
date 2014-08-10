package net.mcthunder.packet.ingame.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerPluginMessagePacket
        implements Packet {
    private String channel;
    private byte[] data;

    private ServerPluginMessagePacket() {
    }

    public ServerPluginMessagePacket(String channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    public String getChannel() {
        return this.channel;
    }

    public byte[] getData() {
        return this.data;
    }

    public void read(NetIn in) throws IOException {
        this.channel = in.readString();
        this.data = in.readBytes(in.readShort());
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.channel);
        out.writeShort(this.data.length);
        out.writeBytes(this.data);
    }

    public boolean isPriority() {
        return false;
    }
}