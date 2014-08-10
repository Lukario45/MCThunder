package net.mcthunder.packet.status.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class StatusPongPacket
        implements Packet {
    private long time;

    private StatusPongPacket() {
    }

    public StatusPongPacket(long time) {
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
