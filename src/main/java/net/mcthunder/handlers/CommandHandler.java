package net.mcthunder.handlers;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
import net.mcthunder.apis.Player;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
/**
 * Created by Kevin on 10/13/2014.
 */
public class CommandHandler {


    public void handlePlayerCommand(Player player, ClientChatPacket packet) throws NullPointerException {
        String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);

        Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");


        cmd.execute(player, packet);

    }

}
