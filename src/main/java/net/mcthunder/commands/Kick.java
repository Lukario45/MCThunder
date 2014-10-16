package net.mcthunder.commands;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import net.mcthunder.handlers.ServerChatHandler;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;

import java.util.List;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Kick extends Command {
    private ServerChatHandler serverChatHandler;

    public Kick() {
        super("kick", "kick", "Kicks a player from the server!", 9999, "command.kick");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        List<Session> sessions = player.getServer().getSessions();
        String[] wholeMessage = packet.getMessage().split(" ");
        serverChatHandler = new ServerChatHandler();
        if (wholeMessage.length >= 2) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 1; i < wholeMessage.length; i++) {
                sb.append(wholeMessage[i]).append(" ");
            }
            String saidName = sb.toString().trim().split(" ")[0];
            for (int i = 2; i < wholeMessage.length; i++) {
                sb2.append(wholeMessage[i]).append(" ");
            }
            String args = sb2.toString().trim();
            Boolean foundName = false;
            for (Session s : sessions) {
                GameProfile p = s.getFlag(ProtocolConstants.PROFILE_KEY);
                String sessionName = p.getName();
                if (sessionName.equalsIgnoreCase(saidName)) {
                    s.disconnect("Kicked: " + args);
                    player.getChatHandler().sendMessage(player.getServer(), sessionName + " was kicked by " + player.gameProfile().getName() + "!");

                    foundName = true;
                    break;
                }


            }
            if (!foundName) {
                player.sendMessageToPlayer("Could not finde player " + saidName + "!");

            }
        } else {
            player.sendMessageToPlayer("Not enough arguments!");


        }
        return true;
    }
}
