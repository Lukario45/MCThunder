package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class Demote extends Command {
    public Demote() {
        super("demote", "Demotes a player to the previous rank", "/demote player", 9999,"rankmanager.setrank.demote");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
