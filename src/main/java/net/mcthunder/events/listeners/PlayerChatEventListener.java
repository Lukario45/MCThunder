package net.mcthunder.events.listeners;

import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerChatEventListenerInterface;
import net.mcthunder.handlers.ServerChatHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerChatEventListener implements PlayerChatEventListenerInterface {
    private ServerChatHandler serverChatHandler;

    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onChat(Player player, ClientChatPacket packet) {
        serverChatHandler = new ServerChatHandler();
        serverChatHandler.handleChat(player, packet);
    }
}