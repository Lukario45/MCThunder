package net.mcthunder.packet.ingame.server.scoreboard;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerScoreboardObjectivePacket
        implements Packet {
    private String name;
    private String displayName;
    private Action action;

    private ServerScoreboardObjectivePacket() {
    }

    public ServerScoreboardObjectivePacket(String name, String displayName, Action action) {
        this.name = name;
        this.displayName = displayName;
        this.action = action;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Action getAction() {
        return this.action;
    }

    public void read(NetIn in) throws IOException {
        this.name = in.readString();
        this.displayName = in.readString();
        this.action = Action.values()[in.readByte()];
    }

    public void write(NetOut out) throws IOException {
        out.writeString(this.name);
        out.writeString(this.displayName);
        out.writeByte(this.action.ordinal());
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Action {
        ADD,
        REMOVE,
        UPDATE;
    }
}