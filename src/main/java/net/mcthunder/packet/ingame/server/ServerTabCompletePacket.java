package net.mcthunder.packet.ingame.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerTabCompletePacket
        implements Packet {
    private String[] matches;

    private ServerTabCompletePacket() {
    }

    public ServerTabCompletePacket(String[] matches) {
        this.matches = matches;
    }

    public String[] getMatches() {
        return this.matches;
    }

    public void read(NetIn in) throws IOException {
        this.matches = new String[in.readVarInt()];
        for (int index = 0; index < this.matches.length; index++)
            this.matches[index] = in.readString();
    }

    public void write(NetOut out)
            throws IOException {
        out.writeVarInt(this.matches.length);
        for (String match : this.matches)
            out.writeString(match);
    }

    public boolean isPriority() {
        return false;
    }
}