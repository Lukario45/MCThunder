package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class SetDefaultRank extends Command {
    public SetDefaultRank() {
        super("SetDefaultRank", "Set the default rank", "/setdefaultrank rank", 9999, "rankmanager.setdefaultrank");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
