package net.mcthunder.packet.status.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class StatusQueryPacket
        implements Packet {
    public void read(NetIn in)
            throws IOException {
    }

    public void write(NetOut out)
            throws IOException {
    }

    public boolean isPriority() {
        return false;
    }
}