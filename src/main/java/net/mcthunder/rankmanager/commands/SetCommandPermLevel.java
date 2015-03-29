package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class SetCommandPermLevel extends Command {
    public SetCommandPermLevel() {
        super("SetCommandPermLevel", "Change the Default perm level for any command", "/setcommandpermlevel command level", 9999, "rankmanager.setcmdpermlevel");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        return false;
    }
}
