package net.mcthunder.handlers;

/**
 * Created by Kevin on 10/13/2014.
 */
public class CommandHandler {


    public void handleCommand() {
        String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);

        Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");
        cmd.execute(server, event.getSession(), packet);

    }

}
