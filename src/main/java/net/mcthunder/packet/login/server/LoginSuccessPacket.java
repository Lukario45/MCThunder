package net.mcthunder.packet.login.server;

import net.mcthunder.auth.GameProfile;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class LoginSuccessPacket
        implements Packet {
    private GameProfile profile;

    private LoginSuccessPacket() {
    }

    public LoginSuccessPacket(GameProfile profile) {
        this.profile = profile;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public void read(NetIn in) throws IOException {
        this.profile = new GameProfile(in.readString(), in.readString());
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.profile.getIdAsString());
        out.writeString(this.profile.getName());
    }

    public boolean isPriority() {
        return true;
    }
}