package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.Player;
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
            player.sendMessage("&4" + getArguments());
        } else {
            String toPlayer = wholeMessage[1];
            Player p = MCThunder.getPlayer(toPlayer);
            if(p == null) {
                player.sendMessage("&cThat player is not online!");
                return true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String message = sb.toString().trim();
            player.sendMessage("&3[You &e---> &3" + p.getName() + "]&e:&r " + message);
            p.sendMessage("&3[" + player.getName() + " &e---> &3You]&e:&r " + message);
            player.setLastPmPerson(p);
            p.setLastPmPerson(player);
        }
        return true;
    }
}