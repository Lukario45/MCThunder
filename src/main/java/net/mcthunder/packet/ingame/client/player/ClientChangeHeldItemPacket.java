package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientChangeHeldItemPacket
        implements Packet {
    private int slot;

    private ClientChangeHeldItemPacket() {
    }

    public ClientChangeHeldItemPacket(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    public void read(NetIn in) throws IOException, IOException {
        this.slot = in.readShort();
    }

    public void write(NetOut out) throws IOException {
        out.writeShort(this.slot);
    }

    public boolean isPriority() {
        return false;
    }
}