package net.mcthunder.handlers;

import net.mcthunder.apis.LoggingLevel;
import org.spacehq.mc.auth.GameProfile;
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

    public void checkPlayer(GameProfile profile) {
        ServerChatHandler chatHandler = new ServerChatHandler();
        String playerID = profile.getIdAsString();
        String playerName = profile.getName();
        tellConsole(LoggingLevel.DEBUG, playerName + " has UUID  " + playerID);
        File playerFile = new File("PlayerFiles", playerID + ".yml");
        if (!playerFile.exists()) {
            tellConsole(LoggingLevel.INFO, "Player: " + playerName + "'s file does not exist yet, creating file!");
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //really, this shouldnt exist, you shouldnt spam console with completion of simple file I/O operations
            tellConsole(LoggingLevel.INFO, "Done!");

            chatHandler.sendMessage(server, "Welcome " + playerName + " to the server!");
        }
    }

    public void addAttribute(GameProfile profile, String attribute, String attributeData) {

    }

    public void deleteAttribute(GameProfile profile, String attribute, String attributeData) {

    }

    public void changeAttribute(GameProfile profile, String attribute, String attributeData) {

    }

    public void getAttribute(GameProfile profile, String attribute, String attributeData) {

    }

    public void addMultiAttribute(GameProfile profile, String attribute, String attributeData) {

    }
}