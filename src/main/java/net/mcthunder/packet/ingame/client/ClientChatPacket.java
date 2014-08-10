package net.mcthunder.packet.ingame.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientChatPacket
        implements Packet {
    private String message;

    private ClientChatPacket() {
    }

    public ClientChatPacket(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void read(NetIn in) throws IOException, IOException {
        this.message = in.readString();
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.message);
    }

    public boolean isPriority() {
        return false;
    }
}
