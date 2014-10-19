package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/19/2014.
 */
public class GetUUID extends Command {

    public GetUUID() {
        super("getuuid", Arrays.asList("getuuid", "uuid"), "Gets the UUID of a player", "/getuuid PLAYER", 9999, "command.getuuid");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length > 2 || wholeMessage.length < 2)
            player.sendMessage("&4" + getArguments());
        else {
            Player p = MCThunder.getPlayer(wholeMessage[1]);
            if (p == null) {
                player.sendMessage("&6Could not find player &c" + wholeMessage[1] + "&6!");
            } else {
                player.sendMessage("&3" + p.gameProfile().getName() + ":&e " + p.gameProfile().getIdAsString());
            }
        }
        return false;
    }
}
