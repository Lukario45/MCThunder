package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

/**
 * Created by Kevin on 10/19/2014.
 */
public class GetUUID extends Command {
    public GetUUID() {
        super("getuuid", Arrays.asList("uuid"), "Gets the UUID of a player", "/getuuid <player>", 9999, "core.getuuid");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        Player p = MCThunder.getPlayer(args[0]);
        player.sendMessage(p == null ? "&6Could not find player &c" + args[0] + "&6!" :"&3" + p.getName() + ":&e " + p.getUniqueID());
        return true;
    }
}