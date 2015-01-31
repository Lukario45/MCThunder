package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import com.Lukario45.NBTFile.Utilities;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 11/12/2014.
 */
public class Rank {

    public void newRank(String name, int points) {
        RankManager rm = new RankManager();
        NBTFile rankFile = new NBTFile(new File("RankManager/ranks.dat"), "Ranks");
        CompoundTag compoundTag = new CompoundTag(name);
        compoundTag.put(Utilities.makeIntTag("CommandLevel", points));
        try {

            rankFile.write(compoundTag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRanks() {

    }

}
