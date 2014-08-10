package net.mcthunder.protocol.packet.ingame.client;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ClientTabCompletePacket implements Packet {

    private String text;

    @SuppressWarnings("unused")
    private ClientTabCompletePacket() {
    }

    public ClientTabCompletePacket(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.text = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.text);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
