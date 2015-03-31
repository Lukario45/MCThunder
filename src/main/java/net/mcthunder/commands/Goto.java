package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import net.mcthunder.world.World;

import java.util.Arrays;

public class Goto extends Command {
    public Goto() {
        super("goto", Arrays.asList("world", "g", "level", "l"), "Teleports you to the specified world.", "/goto <worldname>", 9999, "core.goto");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        World w = MCThunder.getWorld(args[0]);
        if (w == null)
            player.sendMessage("&4Error: Invalid world.");
        else {
            player.teleport(w.getSpawnLocation());
            player.sendMessage("Teleported to world " + w.getName() + ".");
        }
        return true;
    }
}