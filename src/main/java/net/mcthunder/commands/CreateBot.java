package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import net.mcthunder.tests.TestBot;

import java.util.Arrays;

public class CreateBot extends Command {
    public CreateBot() {
        super("createbot", Arrays.asList("bot"), "Creates a bot. Temporary command", "/createbot <name>", 9999, "core.createbot");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        TestBot b = new TestBot(args[0]);
        MCThunder.loadBot(b);
        b.spawn(player.getLocation());
        return true;
    }
}