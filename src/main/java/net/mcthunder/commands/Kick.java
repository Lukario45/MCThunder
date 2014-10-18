package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Kick extends Command {


    public Kick() {
        super("kick", "kick", "Kicks a player from the server!", "/kick PLAYERNAME <reason> ", 9999, "command.kick");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length >= 2) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 1; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String saidName = sb.toString().trim().split(" ")[0];
            for (int i = 2; i < wholeMessage.length; i++)
                sb2.append(wholeMessage[i]).append(" ");
            String args = sb2.toString().trim();
            Boolean foundName = false;
            for (Player p : MCThunder.playerHashMap.values()) {
                String sessionName = p.gameProfile().getName();
                if (sessionName.equalsIgnoreCase(saidName)) {
                    p.getSession().disconnect("Kicked: " + args);
                    player.getChatHandler().sendMessage(player.getServer(), sessionName + " was kicked by " + player.gameProfile().getName() + "!");

                    foundName = true;
                    break;
                }

            }
            if (!foundName)
                player.sendMessageToPlayer("Could not find player " + saidName + "!");
        } else
            player.sendMessageToPlayer(getArguments());
        return true;
    }
}