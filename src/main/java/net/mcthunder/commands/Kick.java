package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Kick extends Command {
    public Kick() {
        super("kick", Arrays.asList(""), "Kicks a player from the server!", "/kick <player> <reason> ", 9999, "command.kick");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length > 1) {
            String saidName = wholeMessage[1];
            Player p = MCThunder.getPlayer(saidName);
            if (p == null)
                player.sendMessage("&6Could not find player &c" + saidName + "&6!");
            else {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < wholeMessage.length; i++)
                    sb.append(wholeMessage[i]).append(" ");
                player.getChatHandler().sendMessage(player.getServer(), "&1" + p.gameProfile().getName() + " &6was kicked by &1" + player.gameProfile().getName() + "&6!");
                p.getSession().disconnect("Kicked: " + sb.toString().trim());
            }
        } else
            player.sendMessage("&4" + getArguments());
        return true;
    }
}