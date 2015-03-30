package net.mcthunder.rankmanager.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

/**
 * Created by conno_000 on 3/29/2015.
 */
public class SetRank extends Command {
    public SetRank() {
        super("SetRank", "Sets a players rank", "/setrank <player> <rank>", 9999, "rankmanager.setrank");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2)
            return false;
        Player p = MCThunder.getPlayer(args[0]);
        if (p == null)
            player.sendMessage("&cThat Player is not online!");
        else if (((CompoundTag) MCThunder.getProfileHandler().getAttribute(p, "RankManager")).get("RankName").getValue().toString().equalsIgnoreCase(args[1]))
            player.sendMessage("&cPlayer is already is that rank!");
        else if (!MCThunder.getRankManager().getRankHashMap().containsKey(args[1]))
            p.sendMessage("&cRank does not exist!");
        else {
            MCThunder.getRankManager().setPlayerRank(p, args[1]);
            player.sendMessage("&1Player " + p.getDisplayName() + "'s rank has been changed!");
            p.sendMessage("Your rank has been set to " + args[1] + "!");
        }
        return true;
    }
}