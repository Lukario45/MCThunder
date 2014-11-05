package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 11/5/2014.
 */
public class Teleport extends Command {
    public Teleport() {
        super("teleport", Arrays.asList("tp"), "teleports players", "/tp player toplayer", 9999, "commands.teleport");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length < 2)
            player.sendMessage("&4" + getArguments());
        else if (wholeMessage.length == 2) {
            Player p = MCThunder.getPlayer(wholeMessage[1]);
            if (p == null)
                player.sendMessage("&cThat player is not online!");
            else
                player.teleport(p.getLocation());
        }
        return true;
    }
}