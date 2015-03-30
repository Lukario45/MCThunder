package net.mcthunder.rankmanager.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

import java.util.Arrays;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class Demote extends Command {
    public Demote() {
        super("demote", Arrays.asList("de"), "Demotes a player to the previous rank", "/demote <player>", 9999, "rankmanager.setrank.demote");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        Player p = MCThunder.getPlayer(args[0]);
        if (p == null) {
            player.sendMessage("&cThat Player is not online!");
            return true;
        }
        CompoundTag c = (CompoundTag) MCThunder.getProfileHandler().getAttribute(p, "RankManager");
        for (int i = MCThunder.getRankManager().getCommandLevelFromRank(c.get("RankName").getValue().toString()) - 1; i >= -9999; i--)
            if (MCThunder.getRankManager().getRankHashMap().containsValue(i)) {
                String rank = (String) MCThunder.getRankManager().getReverseRankHashMap().get(i);
                MCThunder.getRankManager().setPlayerRank(p, rank);
                player.sendMessage("&1Player " + p.getDisplayName() + "'s rank has been changed to " + rank + "!");
                p.sendMessage("Your rank has been set to " + rank + "!");
                return true;
            }
        player.sendMessage("&cThere is no Lower rank!");
        return true;
    }
}