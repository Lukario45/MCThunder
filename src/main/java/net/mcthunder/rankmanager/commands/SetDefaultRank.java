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
        super("SetDefaultRank", "Set the default rank", "/setdefaultrank rank", 9999, "rankmanager.setdefaultrank");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 1){
            return false;
        } else {
            if  (!MCThunder.getRankManager().getRankHashMap().containsKey(args[0])){
                player.sendMessage("&cThat rank does not exist yet!");
                return true;
            } else {
                try {
                    MCThunder.getRankManager().getRanks().write(Utilities.makeStringTag("DefaultRank", args[0]));
                    player.sendMessage("&bDefault rank has been changed to " + args[0] + "!");
                    tellConsole(LoggingLevel.INFO,"&bDefault rank has been changed to " + args[0] + "!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }


        }
    }
}
