package net.mcthunder.handlers;

import net.mcthunder.api.Command;
import net.mcthunder.api.CommandRegistry;
import net.mcthunder.api.Player;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class CommandHandler {
    public void handlePlayerCommand(Player player, ClientChatPacket packet) throws NullPointerException {
        String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);
        Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");
        if (!cmd.execute(player, packet))
            player.sendMessage("&4" + cmd.getArguments());
    }
}