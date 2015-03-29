package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class Promote extends Command {
    public Promote() {
        super("promote", "Promotes a player to the next rank up", "/promote player", 9999, "rankmanager.setrank.promote");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
