package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by Kevin on 10/14/2014.
 */
public class Kick extends Command {
    public Kick() {
        super("kick", "Kicks a player from the server!", "/kick <player> <reason> ", 9999, "command.kick");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        String saidName = args[0];
        Player p = MCThunder.getPlayer(saidName);
        if (p == null)
            player.sendMessage("&6Could not find player &c" + saidName + "&6!");
        else {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            MCThunder.broadcast("&1" + p.getDisplayName() + " &6was kicked by &1" + player.getDisplayName() + "&6!");
            p.disconnect("Kicked: " + sb.toString().trim());
        }
        return true;
    }
}