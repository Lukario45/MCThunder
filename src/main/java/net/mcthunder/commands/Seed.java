package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

public class Seed extends Command {
    public Seed() {
        super("seed", "Retrieves the world seed", "/seed", 9999, "command.seed");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        player.sendMessage("Seed: " + player.getWorld().getSeed());
        return true;
    }
}