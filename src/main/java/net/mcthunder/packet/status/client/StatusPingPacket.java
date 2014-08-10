package net.mcthunder.packet.status.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class StatusPingPacket
        implements Packet {
    private long time;

    private StatusPingPacket() {
    }

    public StatusPingPacket(long time) {
        this.time = time;
    }

    public long getPingTime() {
        return this.time;
    }

    public void read(NetIn in) throws IOException, IOException {
        this.time = in.readLong();
    }

    public void write(NetOut out) throws IOException {
        out.writeLong(this.time);
    }

    public boolean isPriority() {
        return false;
    }
}