package net.mcthunder.handlers;

import org.apache.commons.lang.StringUtils;
import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
/**
 * Created by Kevin on 10/13/2014.
 */
public class CommandHandler {


    public void handleCommand(Server server, Session session, ClientChatPacket packet) {
        String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);

        Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");
        cmd.execute(server, event.getSession(), packet);

    }

}
