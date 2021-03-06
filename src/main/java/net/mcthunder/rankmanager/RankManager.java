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
import java.util.List;
import java.util.Map;

import static net.mcthunder.api.Utils.makeDir;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 11/12/2014.
 */
public class RankManager {
    private Config config;
    private NBTFile ranks;
    private HashMap<String, Integer> rankHashmap;
    private HashMap<Integer, String> reverseRankHashMap;
    private HashMap<Player,PlayerRank> playerRankMap;
    private Rank rank;

    public void load() {
        MCThunder.addCommandPath("net.mcthunder.rankmanager.commands");
        MCThunder.addLoginEventListener(new RankManagerLoggingInEventListener());
        MCThunder.addCommandEventListener(new RankManagerCommandEventListener());
        this.rankHashmap = new HashMap<>();
        this.reverseRankHashMap = new HashMap<>();
        this.playerRankMap = new HashMap<>();
        this.rank = new Rank();
        tellConsole(LoggingLevel.INFO, "Loading Rank Manager");
        makeDir("RankManager");

        this.ranks = new NBTFile(new File("RankManager/ranks.dat"), "Ranks");
        try {
            if (!this.ranks.getNbtFile().exists()) {
                this.ranks.createFile();
                this.ranks.write(Utilities.makeStringTag("DefaultRank", "Default"));
                this.rank.newRank("Default", 1);
                this.rank.newRank("Moderator", 5000);
                this.rank.newRank("Owner", 9999);
            }
            for (Tag t : NBTIO.readFile(this.ranks.getNbtFile(), false)){
                if (t.getName().equalsIgnoreCase("DefaultRank")){

                } else {
                    this.rankHashmap.put(t.getName(),  Integer.parseInt(Utilities.getFromCompound((CompoundTag) t,"CommandLevel").getValue().toString()));
                    this.reverseRankHashMap.put(Integer.parseInt(Utilities.getFromCompound((CompoundTag) t,"CommandLevel").getValue().toString()),t.getName());
                }
            }
            tellConsole(LoggingLevel.INFO,"Loaded " + this.rankHashmap.size() + " Rank(s)");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.config = new Config();
        this.config.loadConfig();
    }

    public void setPlayerRank(Player p, String rank){
        Map<String, Tag> tagMap = new HashMap<String,Tag>();
        tagMap.put("RankName",Utilities.makeStringTag("RankName", rank));
        MCThunder.getProfileHandler().changeAttribute(p, new CompoundTag("RankManager", tagMap));
    }

    public Config getConfig() {
        return this.config;
    }

    public NBTFile getRanks() {
        return this.ranks;
    }

    public HashMap getRankHashMap() {return this.rankHashmap;}
    public HashMap getReverseRankHashMap() {return this.reverseRankHashMap;}
    public HashMap<Player,PlayerRank> getPlayerRankMap() {return this.playerRankMap;}

    public void addSpecialNodeToFile(Player p ,String node){
        CompoundTag c = (CompoundTag) MCThunder.getProfileHandler().getAttribute(p, "RankManager");
        List<Tag> l = (List<Tag>) Utilities.getFromCompound(c, "SpecialPerms").getValue();
        l.add(Utilities.makeStringTag("SpecialPerm", node));
        Map<String,Tag> map = c.getValue();
        map.remove("SpecialPerms");
        map.put("SpecialPerms", Utilities.makeListTag("SpecialPerms", l));
        MCThunder.getProfileHandler().changeAttribute(p,new CompoundTag("RankManager",map));
    }

    public Rank getRank() {return this.rank;}

    public int getCommandLevelFromRank(String rank){
        return rankHashmap.get(rank);
    }

    public String getDefaultRank(){
        try {
            return this.ranks.read("DefaultRank").getValue().toString();
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