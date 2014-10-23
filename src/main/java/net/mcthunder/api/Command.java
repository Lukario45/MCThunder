package net.mcthunder.api;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Based of of Alphabot/Lukabot code that was created by zack6849
 */
public abstract class Command {
    private final String name;
    private final List<String> aliases;
    private final String information;
    private final int rankPoints;
    private final String permissionNode;
    private final String arguments;

    public Command(String name, List<String> aliases, String information, String arguments, int rankPoints, String permissionNode) {
        this.name = name;
        if (aliases == null)
            this.aliases = new ArrayList<String>();
        else
            this.aliases = aliases;
        this.information = information;
        this.rankPoints = rankPoints;
        this.permissionNode = permissionNode;
        this.arguments = arguments;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getAliases() {
        return this.aliases;
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

    public String getArguments() {
        return this.arguments;
    }

    public abstract boolean execute(Player player, ClientChatPacket packet);

}