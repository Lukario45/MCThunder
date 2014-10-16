package net.mcthunder.events.listeners;

import net.mcthunder.apis.Player;
import net.mcthunder.handlers.CommandHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerCommandEventListener implements net.mcthunder.interfaces.PlayerCommandEventListener {
    private CommandHandler commandHandler;

    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onCommand(Player player, ClientChatPacket packet) {
        commandHandler = new CommandHandler();
        try {
            commandHandler.handlePlayerCommand(player, packet);
        } catch (ClassNotFoundException e) {

        }
    }
}
