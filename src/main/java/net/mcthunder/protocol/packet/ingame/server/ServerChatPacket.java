package net.mcthunder.protocol.packet.ingame.server;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;
import net.mcthunder.protocol.data.message.Message;

import java.io.IOException;

public class ServerChatPacket implements Packet {

    private Message message;

    @SuppressWarnings("unused")
    private ServerChatPacket() {
    }

    public ServerChatPacket(String text) {
        this(Message.fromString(text));
    }

    public ServerChatPacket(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.message = Message.fromString(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.message.toJsonString());
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
