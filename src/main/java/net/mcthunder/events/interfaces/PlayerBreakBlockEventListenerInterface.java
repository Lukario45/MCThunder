package net.mcthunder.events.interfaces;

import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 11/12/2014.
 */
public interface PlayerBreakBlockEventListenerInterface {
    public void onBlockBreak(Player player, ClientPlayerActionPacket packet) throws ClassNotFoundException;
}
