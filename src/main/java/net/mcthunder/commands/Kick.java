package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Kick extends Command {


    public Kick() {
        super("kick", Arrays.asList("kick"), "Kicks a player from the server!", "/kick <player> <reason> ", 9999, "command.kick");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length > 1) {
            String saidName = wholeMessage[1];
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String args = sb.toString().trim();
            Player p = MCThunder.getPlayer(saidName);
            if (p == null)
                player.sendMessageToPlayer("Could not find player " + saidName + "!");
            else {
                player.getChatHandler().sendMessage(player.getServer(), p.gameProfile().getName() + " was kicked by " + player.gameProfile().getName() + "!");
                p.getSession().disconnect("Kicked: " + args);
            }
        } else
            player.sendMessageToPlayer(getArguments());
        return true;
    }
}