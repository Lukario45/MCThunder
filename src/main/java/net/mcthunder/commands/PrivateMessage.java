package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/18/2014.
 */
public class PrivateMessage extends Command {
    public PrivateMessage() {
        super("privatemessage", "privatemessage", "sends a private message to a player", "/privatemessage PLAYERNAME MESSAGE", 0, "command.privatemessage");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length < 3) {
            player.sendMessageToPlayer(getArguments());
        } else {
            String toPlayer = wholeMessage[1];
            String fromPlayer = player.gameProfile().getName();
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String message = sb.toString().trim();
            player.sendMessageToPlayer("[You] -> " + toPlayer + ": " + message);
            for (Player p : MCThunder.playerHashMap.values()) {
                if (p.gameProfile().getName().equals(toPlayer)) {
                    p.sendMessageToPlayer("[" + fromPlayer + "] -> You: " + message);

                }

            }
        }

        return false;
    }
}
