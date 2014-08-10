package net.mcthunder.packet.ingame.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientTabCompletePacket
        implements Packet {
    private String text;

    private ClientTabCompletePacket() {
    }

    public ClientTabCompletePacket(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void read(NetIn in) throws IOException, IOException {
        this.text = in.readString();
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.text);
    }

    public boolean isPriority() {
        return false;
    }
}