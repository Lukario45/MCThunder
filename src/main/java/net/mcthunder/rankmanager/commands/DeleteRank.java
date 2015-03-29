package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class DeleteRank extends Command {
    public DeleteRank() {
        super("DeleteRank", Arrays.asList("delrank"), "Delete a rank", "/deleterank rank", 9999, "rankmanager.editrank.delete");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
