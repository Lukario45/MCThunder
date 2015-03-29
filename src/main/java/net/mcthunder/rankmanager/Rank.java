package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import com.Lukario45.NBTFile.Utilities;
import net.mcthunder.MCThunder;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

import java.io.File;
import java.io.IOException;

/**
 * Created by Kevin on 11/12/2014.
 */
public class Rank {
    public void newRank(String name, int points) {
        RankManager rm = MCThunder.getRankManager();
        NBTFile rankFile = rm.getRanks();
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
