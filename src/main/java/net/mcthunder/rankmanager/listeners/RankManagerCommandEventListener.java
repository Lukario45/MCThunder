package net.mcthunder.rankmanager.listeners;

import net.mcthunder.entity.Player;
import net.mcthunder.handlers.CommandHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class RankManagerCommandEventListener implements net.mcthunder.interfaces.PlayerCommandEventListener {
    private CommandHandler commandHandler;

    @Override
    public boolean removeDefaultListener() {
        return true;
    }

    @Override
    public void onCommand(Player player, ClientChatPacket packet) {
        commandHandler = new CommandHandler();
        try {
            commandHandler.handlePlayerCommand(player, packet);
        } catch (NullPointerException e) {
            player.sendMessage("&4Unknown Command!");
        }
    }
}