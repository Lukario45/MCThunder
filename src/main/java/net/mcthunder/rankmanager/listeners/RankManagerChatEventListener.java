package net.mcthunder.rankmanager.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerChatEventListenerInterface;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class RankManagerChatEventListener implements PlayerChatEventListenerInterface {
    @Override
    public boolean removeDefaultListener() {
        return true;
    }

    @Override
    public void onChat(Player player, ClientChatPacket packet) {
        MCThunder.getChatHandler().handleChat(player, packet);
    }
}