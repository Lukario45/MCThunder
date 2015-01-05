package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

public class Nick extends Command {
    public Nick() {
        super("nick", Arrays.asList("nickname"), "Changes your nickname", "/nick <newname>", 9999, "command.nick");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        player.setDisplayName(args.length == 0 ? null : args[0]);
        player.sendMessage("Nickname updated.");
        return true;
    }
}