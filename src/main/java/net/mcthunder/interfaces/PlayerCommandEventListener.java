package net.mcthunder.interfaces;

import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/14/2014.
 */
public interface PlayerCommandEventListener {
    public boolean removeDefaultListener();

    public void onCommand(Player player, ClientChatPacket packet) throws ClassNotFoundException;
}