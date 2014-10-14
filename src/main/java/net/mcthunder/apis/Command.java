package net.mcthunder.apis;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 10/13/2014.
 */
public abstract class Command {
    private final String name;
    private final String alisis;
    private final String information;
    private final int rankPoints;
    private final String permissionNode;

    public Command(String name, String alisis, String information, int rankPoints, String permissionNode) {
        this.name = name;
        this.alisis = alisis;
        this.information = information;
        this.rankPoints = rankPoints;
        this.permissionNode = permissionNode;
    }

    public String getName() {
        return this.name;
    }

    public String getAlisis() {
        return this.alisis;
    }

    public String getInformation() {
        return this.information;
    }

    public int getRankPoints() {
        return this.rankPoints;
    }

    public String getPermissionNode() {
        return this.permissionNode;
    }

    public abstract boolean execute(Server server, Session session, ClientChatPacket packet);


}
