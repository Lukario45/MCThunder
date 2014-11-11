package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/19/2014.
 */
public class GetUUID extends Command {
    public GetUUID() {
        super("getuuid", Arrays.asList("uuid"), "Gets the UUID of a player", "/getuuid <player>", 9999, "command.getuuid");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length != 2)
            return false;
        Player p = MCThunder.getPlayer(wholeMessage[1]);
        if (p == null)
            player.sendMessage("&6Could not find player &c" + wholeMessage[1] + "&6!");
        else
            player.sendMessage("&3" + p.getName() + ":&e " + p.getUniqueID());
        return true;
    }
}