package net.mcthunder.rankmanager.listeners;

import net.mcthunder.api.Player;
import net.mcthunder.handlers.ServerChatHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class RankManagerChatEventListener implements net.mcthunder.interfaces.PlayerChatEventListener {
    private ServerChatHandler serverChatHandler;

    @Override
    public boolean removeDefaultListener() {
        return true;
    }

    @Override
    public void onChat(Player player, ClientChatPacket packet) {
        serverChatHandler = new ServerChatHandler();
        serverChatHandler.handleChat(player, packet);
    }
}