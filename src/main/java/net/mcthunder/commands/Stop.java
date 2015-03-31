package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

/**
 * Created by Kevin on 10/13/2014.
 */
public class Stop extends Command {
    public Stop() {
        super("stop", Arrays.asList(""), "Stops the server", "/stop <message>", 9999, "core.stop");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        String message = "Server Closed!";
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String arg : args)
                sb.append(arg).append(" ");
            message = sb.toString().trim();
        }
        MCThunder.shutdown(message);
        return true;
    }
}