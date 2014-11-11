package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;

import java.io.File;
import java.io.IOException;

import static net.mcthunder.api.Utils.tellConsole;


/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerProfileHandler {
    public void checkPlayer(Player player) {
        String playerID = player.getUniqueID().toString();
        String playerName = player.getName();
        tellConsole(LoggingLevel.DEBUG, playerName + " has an ID of " + playerID);
        File playerFile = new File("PlayerFiles", playerID + ".yml");
        if (!playerFile.exists()) {
            tellConsole(LoggingLevel.DEBUG, "Player: " + playerName + "'s file does not exist yet, creating file!");
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + playerName + " &bto the server!");
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