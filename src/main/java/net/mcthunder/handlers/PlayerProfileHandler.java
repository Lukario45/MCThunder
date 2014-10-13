package net.mcthunder.handlers;

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
        tellConsole("DEBUG", playerName + " has an ID of " + playerID);
        File playerFile = new File("PlayerFiles", playerID + ".yml");
        if (!playerFile.exists()) {
            tellConsole("INFO", "Player: " + playerName + "'s file does not exist yet, creating file!");
            chatHandler.sendMessage(server, "Welcome " + playerName + " to the server!");
            //    playerFile.mkdirs();

            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
