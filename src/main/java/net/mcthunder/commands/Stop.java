package net.mcthunder.commands;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;

import java.util.List;

/**
 * Created by Kevin on 10/13/2014.
 */
public class Stop extends Command {
    public Stop() {
        super("stop", "stop", "Stops the server", 9999, "command.stop");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        List<Session> sessions = player.getServer().getSessions();
        String[] wholeMessage = packet.getMessage().split(" ");

        if (wholeMessage.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            String args = sb.toString().trim();
            for (Session s : sessions)
                s.disconnect(args);
            player.getServer().close();
        } else {
            for (Session s : sessions)
                s.disconnect("Server Closed!");
            player.getServer().close();
        }
        return true;
    }
}