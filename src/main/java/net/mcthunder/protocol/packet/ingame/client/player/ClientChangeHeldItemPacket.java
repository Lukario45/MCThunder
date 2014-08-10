package net.mcthunder.protocol.packet.ingame.client.player;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ClientChangeHeldItemPacket implements Packet {

    private int slot;

    @SuppressWarnings("unused")
    private ClientChangeHeldItemPacket() {
    }

    public ClientChangeHeldItemPacket(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return this.slot;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.slot = in.readShort();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeShort(this.slot);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
