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
        super("respond", Arrays.asList("respond", "r"), "responds to the last persion who pmed you", "/r message", 0, "command.respond");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        Player p = player.getLastPmPersion();
        if (p == null) {
            player.sendMessageToPlayer("No one to respond to!");
        } else {
            if (wholeMessage.length < 2) {
                player.sendMessageToPlayer(getArguments());
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < wholeMessage.length; i++)
                    sb.append(wholeMessage[i]).append(" ");
                String message = sb.toString().trim();
                p.sendMessageToPlayer("[" + player.gameProfile().getName() + "] ->  You: " + message);
                player.sendMessageToPlayer("[You] -> " + p.gameProfile().getName() + ": " + message);
                p.setLastPmPersion(player);
            }
        }


        return false;
    }
}
