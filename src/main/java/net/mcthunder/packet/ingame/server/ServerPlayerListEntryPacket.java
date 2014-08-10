package net.mcthunder.packet.ingame.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerPlayerListEntryPacket
        implements Packet {
    private String name;
    private boolean online;
    private int ping;

    private ServerPlayerListEntryPacket() {
    }

    public ServerPlayerListEntryPacket(String name, boolean online, int ping) {
        this.name = name;
        this.online = online;
        this.ping = ping;
    }

    public String getName() {
        return this.name;
    }

    public boolean getOnline() {
        return this.online;
    }

    public int getPing() {
        return this.ping;
    }

    public void read(NetIn in) throws IOException {
        this.name = in.readString();
        this.online = in.readBoolean();
        this.ping = in.readShort();
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.name);
        out.writeBoolean(this.online);
        out.writeShort(this.ping);
    }

    public boolean isPriority() {
        return false;
    }
}