package net.mcthunder.rankmanager.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import net.mcthunder.rankmanager.Rank;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class Promote extends Command {
    private String rank;

    public Promote() {
        super("promote", "Promotes a player to the next rank up", "/promote player", 9999, "rankmanager.setrank.promote");
    }

    @Override
    public boolean execute(Player player, String[] args) {


        if (args.length < 1) {
            return false;
        } else {
            try {
                Player p = MCThunder.getPlayer(args[0]);
                boolean running;
                CompoundTag c = (CompoundTag) MCThunder.getProfileHandler().getAttribute(p, "RankManager");
                int i = MCThunder.getRankManager().getCommandLevelFromRank(c.get("RankName").getValue().toString()) + 1;



                running = true;
                while (running) {

                    if (i > 9999) {
                        player.sendMessage("&cThere is no Higher rank!");
                        return true;
                    }
                    if (MCThunder.getRankManager().getRankHashMap().containsValue(i)) {
                        rank = (String) MCThunder.getRankManager().getReverseRankHashMap().get(i); MCThunder.getRankManager().setPlayerRank(p, rank);
                        player.sendMessage("&1Player " + p.getDisplayName() + "'s rank has been changed to " + rank + "!");
                        p.sendMessage("Your rank has been set to " + rank + "!");
                        return true;

                    }
                    i++;


                } return true;


            } catch (NullPointerException e) {
                player.sendMessage("&cThat Player is not online!");
                return true;
            }

        }

    }
}
