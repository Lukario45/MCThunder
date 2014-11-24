package net.mcthunder.rankmanager;

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
    private File ranks;
    private RankManagerLoggingInEventListener loginEventListener;
    private PlayerLoggingInEventSource loggingInEventSource;

    public void load() {
        loggingInEventSource = new PlayerLoggingInEventSource();
        loginEventListener = new RankManagerLoggingInEventListener();
        MCThunder.addLoginEventListener(loginEventListener);
        Rank rank = new Rank();
        tellConsole(LoggingLevel.INFO, "Loading Rank Manager");
        makeDir("RankManager");
        ranks = new File("RankManager", "ranks.dat");
        if (!ranks.exists()) {
            try {
                ranks.createNewFile();
                rank.newRank("Default", 1);
                rank.newRank("Moderator", 5000);
                rank.newRank("Owner,", 9999);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = new Config();
        config.loadConfig();

    }

    public Config getConfig() {
        return this.config;
    }

    public File getRanks() {
        return this.ranks;
    }

    public void deny(Player player, Command command) {
        player.sendMessage(config.getDenyColor() + config.getDenyMessage());
        tellConsole(LoggingLevel.WARNING, player.getName() + " was denied access to " + command.getName());
    }
}
