package net.mcthunder.handlers;

import net.mcthunder.apis.LoggingLevel;
import net.mcthunder.apis.Player;
import org.spacehq.packetlib.Server;

import java.io.File;
import java.io.IOException;

import static net.mcthunder.apis.Utils.tellConsole;


/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerProfileHandler {
    private final Server server;

    public PlayerProfileHandler(Server server) {
        this.server = server;
    }

    public void checkPlayer(Player player) {
        String playerID = player.gameProfile().getIdAsString();
        String playerName = player.gameProfile().getName();
        tellConsole(LoggingLevel.DEBUG, playerName + " has an ID of " + playerID);
        File playerFile = new File("PlayerFiles", playerID + ".yml");
        if (!playerFile.exists()) {
            tellConsole(LoggingLevel.DEBUG, "Player: " + playerName + "'s file does not exist yet, creating file!");
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //we dont need to tell owners about simple file IO completing
            player.getChatHandler().sendMessage(server, "Welcome " + playerName + " to the server!");
        }
    }

    public void addAttribute(Player player, String attribute, String attributeData) {

    }

    public void deleteAttribute(Player player, String attribute) {

    }

    public void changeAttribute(Player player, String attribute, String attributeData) {

    }

    public void getAttribute(Player player, String attribute, String attributeData) {

    }

    public void addMultiAttribute(Player player, String attribute, String attributeData) {

    }
}