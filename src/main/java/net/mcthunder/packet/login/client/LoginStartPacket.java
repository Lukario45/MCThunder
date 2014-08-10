package net.mcthunder.packet.login.client;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class LoginStartPacket
        implements Packet {
    private String username;

    private LoginStartPacket() {
    }

    public LoginStartPacket(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void read(NetIn in) throws IOException {
        this.username = in.readString();
    }

    public void write(NetOut out) throws IOException, IOException {
        out.writeString(this.username);
    }

    public boolean isPriority() {
        return true;
    }
}