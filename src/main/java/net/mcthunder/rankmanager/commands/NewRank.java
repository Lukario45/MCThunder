package net.mcthunder.rankmanager.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class NewRank extends Command {
    public NewRank() {
        super("NewRank", "Create a new rank", "/newrank name commandlevel (other variables and values)", 9999, "rankmanager.editrank.new");
    }

    @Override
    public boolean execute(Player player, String[] args) {

        if (args.length < 2) {
            return false;
        } else {
            try {
                MCThunder.getRankManager().getRank().newRank(args[0] ,Integer.parseInt(args[1]));

                return true;
            } catch (NumberFormatException e){
                player.sendMessage("&cCommandlevel must be an Integer!");
                return false;
            }


        }

    }
}
