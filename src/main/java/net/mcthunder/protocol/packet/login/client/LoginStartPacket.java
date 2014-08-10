package net.mcthunder.protocol.packet.login.client;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class LoginStartPacket implements Packet {

    private String username;

    @SuppressWarnings("unused")
    private LoginStartPacket() {
    }

    public LoginStartPacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.username = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.username);
    }

    @Override
    public boolean isPriority() {
        return true;
    }

}
