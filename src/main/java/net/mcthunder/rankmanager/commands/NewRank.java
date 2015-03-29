package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class NewRank extends Command {
    public NewRank() {
        super("NewRank", "Create a new rank", "/newrank name commandlevel (other variables and values)", 9999, "rankmanager.editrank.new");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
