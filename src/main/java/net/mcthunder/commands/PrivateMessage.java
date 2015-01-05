package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

/**
 * Created by Kevin on 10/18/2014.
 */
public class PrivateMessage extends Command {
    public PrivateMessage() {
        super("privatemessage", Arrays.asList("message", "msg", "pm", "tell"), "Sends a private message to a player", "/privatemessage <player> <message to send>", 0, "command.privatemessage");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2)
            return false;
        String toPlayer = args[0];
        Player p = MCThunder.getPlayer(toPlayer);
        if(p == null) {
            player.sendMessage("&cThat player is not online!");
            return true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++)
            sb.append(args[i]).append(" ");
        String message = sb.toString().trim();
        player.sendMessage("&3[You &e---> &3" + p.getName() + "]&e:&r " + message);
        p.sendMessage("&3[" + player.getName() + " &e---> &3You]&e:&r " + message);
        player.setLastPmPerson(p);
        p.setLastPmPerson(player);
        return true;
    }
}