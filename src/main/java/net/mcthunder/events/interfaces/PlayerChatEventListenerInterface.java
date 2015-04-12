package net.mcthunder.events.interfaces;

import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public interface PlayerChatEventListenerInterface {
    public boolean removeDefaultListener();

    public void onChat(Player player, ClientChatPacket packet);
}