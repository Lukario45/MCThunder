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
            player.sendMessage("No one to respond to!");
            return true;
        }
        if (wholeMessage.length < 2)
            player.sendMessage(getArguments());
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String message = sb.toString().trim();
            p.sendMessage("[" + player.gameProfile().getName() + "] ->  You: " + message);
            player.sendMessage("[You] -> " + p.gameProfile().getName() + ": " + message);
            p.setLastPmPerson(player);
        }
        return true;
    }
}
