package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 11/12/2014.
 */
public class Rank {

    public void newRank(String name, int points) {
        RankManager rm = new RankManager();
        NBTFile rankFile = rm.getRanks();
        Map<String, Tag> compoundTaghashMap = new HashMap<>();
        IntTag level = new IntTag("CommandLevel", points);
        compoundTaghashMap.put(level.getName(), level);
        CompoundTag c = new CompoundTag(name, compoundTaghashMap);
        //  try {
        //NBTIO.writeFile(c,rankFile,false);
        // } catch (IOException e) {

        //  }

    }

    public void getRanks() {

    }

}
