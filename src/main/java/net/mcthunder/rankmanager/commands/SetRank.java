package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class SetRank extends Command {


    public SetRank() {
        super("SetRank", "Sets a players rank", "/setrank player rank", 9999, "rankmanager.setrank");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
