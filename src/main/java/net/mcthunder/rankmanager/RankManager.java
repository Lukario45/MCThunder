package net.mcthunder.rankmanager;

import com.Lukario45.NBTFile.NBTFile;
import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import net.mcthunder.events.source.PlayerLoggingInEventSource;
import net.mcthunder.rankmanager.listeners.RankManagerLoggingInEventListener;

import java.io.File;
import java.io.IOException;

import static net.mcthunder.api.Utils.makeDir;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 11/12/2014.
 */
public class RankManager {
    private Config config;
    private NBTFile ranks;
    private RankManagerLoggingInEventListener loginEventListener;
    private PlayerLoggingInEventSource loggingInEventSource;

    public void load() {
        loggingInEventSource = new PlayerLoggingInEventSource();
        loginEventListener = new RankManagerLoggingInEventListener();
        MCThunder.addLoginEventListener(this.loginEventListener);
        Rank rank = new Rank();
        tellConsole(LoggingLevel.INFO, "Loading Rank Manager");
        makeDir("RankManager");
        ranks = new NBTFile(new File("RankManager/ranks.dat"), "Ranks");
        try {
            ranks.createFile();
            rank.newRank("Default", 1);
            rank.newRank("Moderator", 5000);
            rank.newRank("Owner", 9999);
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
        return ranks;
    }

    public void deny(Player player, Command command) {
        player.sendMessage(this.config.getDenyColor() + this.config.getDenyMessage());
        tellConsole(LoggingLevel.DENY, player.getName() + " was denied access to " + command.getName());
    }
}
