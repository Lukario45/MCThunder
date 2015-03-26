package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import com.Lukario45.NBTFile.Utilities;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kevin on 11/12/2014.
 */
public class Rank {
    public void newRank(String name, int points, boolean isDefault) {
        RankManager rm = new RankManager();
        NBTFile rankFile = new NBTFile(new File("RankManager/ranks.dat"), "Ranks");
        CompoundTag compoundTag = new CompoundTag(name);
        compoundTag.put(Utilities.makeIntTag("CommandLevel", points));
        compoundTag.put(Utilities.makeStringTag("isDefault", String.valueOf(isDefault)));
        try {
            rankFile.write(compoundTag);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRanks() {

    }
}
