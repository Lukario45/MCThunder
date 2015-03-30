package net.mcthunder.rankmanager.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class NewRank extends Command {
    public NewRank() {
        super("NewRank", "Create a new rank", "/newrank <name> <commandlevel> (other variables and values)", 9999, "rankmanager.editrank.new");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2)
            return false;
        String name = args[0];
        if (MCThunder.getRankManager().getRankHashMap().containsKey(name))
            player.sendMessage("&cRank &a'" + name + "' &calready exists!");
        else {
            int cLevel;
            try {
                cLevel = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                player.sendMessage("&cCommandlevel must be an Integer!");
                return true;
            }
            MCThunder.getRankManager().getRank().newRank(name, cLevel);
            MCThunder.getRankManager().getRankHashMap().put(name, cLevel);
            MCThunder.getRankManager().getReverseRankHashMap().put(cLevel, name);
            player.sendMessage("&aNew rank, " + name + ", has successfully been created!");
        }
        return true;
    }
}