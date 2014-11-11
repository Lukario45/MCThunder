package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/13/2014.
 */
public class Stop extends Command {
    public Stop() {
        super("stop", Arrays.asList(""), "Stops the server", "/stop <message>", 9999, "command.stop");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        String args = "Server Closed!";
        if (wholeMessage.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < wholeMessage.length; i++)
                sb.append(wholeMessage[i]).append(" ");
            args = sb.toString().trim();
        }
        MCThunder.shutdown(args);
        return true;
    }
}