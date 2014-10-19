package net.mcthunder.commands;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/18/2014.
 */
public class Respond extends Command {
    public Respond() {
        super("respond", Arrays.asList("reply", "r"), "responds to the last persion who pmed you", "/r message", 0, "command.respond");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        Player p = player.getLastPmPerson();
        if (p == null) {
            player.sendMessage("&cNo one to respond to!");
            return true;
        }
        if (wholeMessage.length < 2)
            player.sendMessage("&4" + getArguments());
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String message = sb.toString().trim();
            player.sendMessage("&3[You] &e-> &3" + p.gameProfile().getName() + ":&e " + message);
            p.sendMessage("&3[" + player.gameProfile().getName() + "] &e-> &3You: &e" + message);
            p.setLastPmPerson(player);
        }
        return true;
    }
}
