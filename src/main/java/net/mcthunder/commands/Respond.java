package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

/**
 * Created by Kevin on 10/18/2014.
 */
public class Respond extends Command {
    public Respond() {
        super("respond", Arrays.asList("reply", "r"), "responds to the last persion who pmed you", "/r message", 0, "core.respond");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        Player p = player.getLastPmPerson();
        if (p == null) {
            player.sendMessage("&cNo one to respond to!");
            return true;
        }
        if (args.length == 0)
            return false;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++)
            sb.append(args[i]).append(" ");
        String message = sb.toString().trim();
        player.sendMessage("&3[You &e---> &3" + p.getName() + "]:&r " + message);
        p.sendMessage("&3[" + player.getName() + " &e---> &3You]:&r " + message);
        p.setLastPmPerson(player);
        return true;
    }
}