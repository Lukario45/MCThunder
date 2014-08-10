package net.mcthunder.packet.ingame.server.scoreboard;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerDisplayScoreboardPacket
        implements Packet {
    private Position position;
    private String name;

    private ServerDisplayScoreboardPacket() {
    }

    public ServerDisplayScoreboardPacket(Position position, String name) {
        this.position = position;
        this.name = name;
    }

    public Position getPosition() {
        return this.position;
    }

    public String getScoreboardName() {
        return this.name;
    }

    public void read(NetIn in) throws IOException {
        this.position = Position.values()[in.readByte()];
        this.name = in.readString();
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.position.ordinal());
        out.writeString(this.name);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Position {
        PLAYER_LIST,
        SIDEBAR,
        BELOW_NAME;
    }
}