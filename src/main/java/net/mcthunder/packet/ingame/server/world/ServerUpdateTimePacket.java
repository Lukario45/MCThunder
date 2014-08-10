package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerUpdateTimePacket
        implements Packet {
    private long age;
    private long time;

    private ServerUpdateTimePacket() {
    }

    public ServerUpdateTimePacket(long age, long time) {
        this.age = age;
        this.time = time;
    }

    public long getWorldAge() {
        return this.age;
    }

    public long getTime() {
        return this.time;
    }

    public void read(NetIn in) throws IOException {
        this.age = in.readLong();
        this.time = in.readLong();
    }

    public void write(NetOut out) throws IOException {
        out.writeLong(this.age);
        out.writeLong(this.time);
    }

    public boolean isPriority() {
        return false;
    }
}