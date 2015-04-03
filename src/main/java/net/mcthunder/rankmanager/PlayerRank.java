package net.mcthunder.rankmanager;

import net.mcthunder.MCThunder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by conno_000 on 4/1/2015.
 */
public class PlayerRank {
    private String rank;
    private List<String> specialPerms;

    public PlayerRank(String rank){
        this.rank = rank;
        this.specialPerms = new ArrayList<>();
    }

    public int containsSpecialPerm(String node){
        return this.specialPerms.contains(node) ? 0 : this.specialPerms.contains("-" + node) ? 1 : 2;
    }

    public void addSpecialPerm(String node){
        this.specialPerms.add(node);
    }

    public String getRank(){
        return this.rank;
    }

    public int getRankLevel(){
        return (int) MCThunder.getRankManager().getRankHashMap().get(this.rank);
    }

    public void setRank(String rank){
        this.rank = rank;
    }
}