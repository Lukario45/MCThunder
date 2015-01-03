package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
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
        String[] wholeMessage = packet.getMessage().trim().split(" ");
        if (wholeMessage.length < 2)
            return false;
        String saidName = wholeMessage[1];
        Player p = MCThunder.getPlayer(saidName);
        if (p == null)
            player.sendMessage("&6Could not find player &c" + saidName + "&6!");
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            MCThunder.broadcast("&1" + p.getDisplayName() + " &6was kicked by &1" + player.getDisplayName() + "&6!");
            p.disconnect("Kicked: " + sb.toString().trim());
        }
        return true;
    }
}