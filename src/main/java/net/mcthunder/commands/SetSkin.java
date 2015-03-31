package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

public class SetSkin extends Command {
    public SetSkin() {
        super("setskin", Arrays.asList("skin"), "Sets your skin to another players", "/setskin <optional player>", 9999, "core.setskin");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            player.removeSkin();
        else
            player.setSkin(args[0]);
        player.sendMessage("Skin updated.");
        return true;
    }
}