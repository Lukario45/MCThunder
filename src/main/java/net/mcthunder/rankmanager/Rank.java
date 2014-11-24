package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import net.mcthunder.handlers.PlayerProfileHandler;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.Tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 11/12/2014.
 */
public class Rank {

    public void newRank(String name, int points) {
        Map<String, Tag> compoundTaghashMap = new HashMap<>();
        IntTag level = new IntTag("CommandLevel", points);
        compoundTaghashMap.put(level.getName(), level);
        CompoundTag c = new CompoundTag(name, compoundTaghashMap);
    }

    public CompoundTag getRank(String rankName) {
        NBTFile rankFile = new NBTFile("RankManager/ranks.dat","Ranks");
        try {
            CompoundTag rank = (CompoundTag) rankFile.read(rankName);
            return rank;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public int getPlayerPoints(Player player){
        PlayerProfileHandler playerProfile = new PlayerProfileHandler();
        int points = ((IntTag)playerProfile.getAttribute(player, "points")).getValue();
        return points;
    }
    public int getCommandPoints(Command c){
        return c.getRankPoints();
    }
    public int getRankPoints(String rankName){
        return 0;


    }
}

