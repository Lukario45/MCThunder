package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import net.mcthunder.world.World;

import java.util.Arrays;

public class Worlds extends Command {
    public Worlds() {
        super("worlds", Arrays.asList("levels"), "Lists the loaded worlds.", "/worlds", 0, "core.worlds");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        String worlds = "Loaded worlds are: ";
        for (World w : MCThunder.getWorlds())
            worlds += w.getName() + ", ";
        if (worlds.equals("Loaded worlds are: "))
            player.sendMessage("&4Error: No worlds are loaded. How are you on the server?");
        else
            player.sendMessage(worlds.substring(0, worlds.length() - 2) + ".");
        return true;
    }
}