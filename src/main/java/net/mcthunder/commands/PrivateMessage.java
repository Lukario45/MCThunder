package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/18/2014.
 */
public class PrivateMessage extends Command {
    public PrivateMessage() {
        super("privatemessage", Arrays.asList("message", "msg", "pm", "tell"), "Sends a private message to a player", "/privatemessage <player> <message to send>", 0, "command.privatemessage");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length < 3) {
            player.sendMessageToPlayer(getArguments());
        } else {
            String toPlayer = wholeMessage[1];
            String fromPlayer = player.gameProfile().getName();
            Player p = MCThunder.getPlayer(toPlayer);
            if(p == null) {
                player.sendMessageToPlayer("That player is not online!");
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String message = sb.toString().trim();
            player.sendMessageToPlayer("[You] -> " + p.gameProfile().getName() + ": " + message);
            p.sendMessageToPlayer("[" + fromPlayer + "] -> You: " + message);
            player.setLastPmPersion(p);
            p.setLastPmPersion(player);
        }
        return true;
    }
}