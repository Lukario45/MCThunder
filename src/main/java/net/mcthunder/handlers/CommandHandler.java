package net.mcthunder.handlers;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
/**
 * Created by Kevin on 10/13/2014.
 */
public class CommandHandler {


    public void handlePlayerCommand(Server server, Session session, ClientChatPacket packet) {
        String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);

        Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");
        cmd.execute(server, session, packet);

    }

}
