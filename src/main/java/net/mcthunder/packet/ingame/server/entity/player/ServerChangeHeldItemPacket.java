package net.mcthunder.packet.ingame.server.entity.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerChangeHeldItemPacket
        implements Packet {
    private int slot;

    private ServerChangeHeldItemPacket() {
    }

    public ServerChangeHeldItemPacket(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    public void read(NetIn in) throws IOException, IOException {
        this.slot = in.readByte();
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.slot);
    }

    public boolean isPriority() {
        return false;
    }
}