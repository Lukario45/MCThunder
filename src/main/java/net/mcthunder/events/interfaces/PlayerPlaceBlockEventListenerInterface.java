package net.mcthunder.events.interfaces;

import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;

/**
 * Created by Kevin on 11/12/2014.
 */
public interface PlayerPlaceBlockEventListenerInterface {
    public void onBlockPlace(Player player, ClientPlayerPlaceBlockPacket packet) throws ClassNotFoundException;
}
