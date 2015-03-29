package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import com.Lukario45.NBTFile.Utilities;
import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import net.mcthunder.rankmanager.listeners.RankManagerCommandEventListener;
import net.mcthunder.rankmanager.listeners.RankManagerLoggingInEventListener;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.Tag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static net.mcthunder.api.Utils.makeDir;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 11/12/2014.
 */
public class RankManager {
    private Config config;
    private NBTFile ranks;
    private HashMap<String, Integer> rankHashmap;
    private Rank rank;

    public void load() {
        MCThunder.addLoginEventListener(new RankManagerLoggingInEventListener());
        MCThunder.addCommandEventListener(new RankManagerCommandEventListener());
        rankHashmap = new HashMap<>();
        rank = new Rank();
        tellConsole(LoggingLevel.INFO, "Loading Rank Manager");
        makeDir("RankManager");

        this.ranks = new NBTFile(new File("RankManager/ranks.dat"), "Ranks");
        try {
            if (!ranks.getNbtFile().exists()) {
                ranks.createFile();
                ranks.write(Utilities.makeStringTag("DefaultRank", "Default"));
                rank.newRank("Default", 1);
                rank.newRank("Moderator", 5000);
                rank.newRank("Owner", 9999);
            }
            for (Tag t : NBTIO.readFile(ranks.getNbtFile(), false)){
                if (t.getName().equalsIgnoreCase("DefaultRank")){


                } else {
                    rankHashmap.put(t.getName(),  Integer.parseInt(Utilities.getFromCompound((CompoundTag) t,"CommandLevel").getValue().toString()));


                }
            }
            tellConsole(LoggingLevel.INFO,"Loaded " + rankHashmap.size() + " Rank(s)");
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = new Config();
        config.loadConfig();

    }

    public Config getConfig() {
        return this.config;
    }

    public NBTFile getRanks() {
        return this.ranks;
    }

    public Rank getRank() {return this.rank;}

    public int getCommandLevelFromRank(String rank){
        return rankHashmap.get(rank);
    }

    public String getDefaultRank(){
        try {
            return ranks.read("DefaultRank").getValue().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deny(Player player, Command command) {
        player.sendMessage(this.config.getDenyColor() + this.config.getDenyMessage());
        tellConsole(LoggingLevel.DENY, player.getName() + " was denied access to " + command.getName());
    }
}
