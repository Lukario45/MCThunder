package net.mcthunder.rankmanager.commands;

import com.Lukario45.NBTFile.Utilities;
import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;

import java.io.IOException;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class SetDefaultRank extends Command {
    public SetDefaultRank() {
        super("SetDefaultRank", "Set the default rank", "/setdefaultrank <rank>", 9999, "rankmanager.setdefaultrank");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        String name = args[0];
        if (!MCThunder.getRankManager().getRankHashMap().containsKey(name))
            player.sendMessage("&cThat rank does not exist yet!");
        else {
            try {
                MCThunder.getRankManager().getRanks().write(Utilities.makeStringTag("DefaultRank", name));
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.sendMessage("&bDefault rank has been changed to " + name + "!");
            tellConsole(LoggingLevel.INFO, "&bDefault rank has been changed to " + name + "!");
        }
        return true;
    }
}