package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class EditRank extends Command {
    public EditRank() {
        super("EditRank", "Edit specific rank variables.", "/editrank <rank> <variable> <value>", 9999, "rankmanager.editrank");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}