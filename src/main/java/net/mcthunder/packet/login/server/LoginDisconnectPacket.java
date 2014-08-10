package net.mcthunder.packet.login.server;

import net.mcthunder.message.Message;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class LoginDisconnectPacket
        implements Packet {
    private Message message;

    private LoginDisconnectPacket() {
    }

    public LoginDisconnectPacket(String text) {
        this(Message.fromString(text));
    }

    public LoginDisconnectPacket(Message message) {
        this.message = message;
    }

    public Message getReason() {
        return this.message;
    }

    public void read(NetIn in) throws IOException {
        this.message = Message.fromString(in.readString());
    }

    public void write(NetOut out) throws IOException, IOException {
        out.writeString(this.message.toJsonString());
    }

    public boolean isPriority() {
        return true;
    }
}