package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

/**
 * Created by Kevin on 11/5/2014.
 */
public class Teleport extends Command {
    public Teleport() {
        super("teleport", Arrays.asList("tp"), "teleports players", "/tp player toplayer", 9999, "core.teleport");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        Player toSend = MCThunder.getPlayer(args[0]);
        Player sendTo = args.length > 1 ? MCThunder.getPlayer(args[1]) : null;
        if (toSend == null)
            player.sendMessage("&cThat player is not online!");
        else {
            if (sendTo == null) {
                sendTo = toSend;
                toSend = player;
            }
            toSend.teleport(sendTo.getLocation());
        }
        return true;
    }
}