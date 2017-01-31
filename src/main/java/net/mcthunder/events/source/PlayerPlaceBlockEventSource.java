package net.mcthunder.events.source;

import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerPlaceBlockEventListenerInterface;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerPlaceBlockEventSource {
    private List<PlayerPlaceBlockEventListenerInterface> playerPlaceBlockEventListeners= new ArrayList<>();

    public synchronized void addEventListener(PlayerPlaceBlockEventListenerInterface listener) {
        this.playerPlaceBlockEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerPlaceBlockEventListenerInterface listener) {
        this.playerPlaceBlockEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Player player, ClientPlayerPlaceBlockPacket packet) throws ClassNotFoundException {
        for (Object playerPlaceBlockEventListener : playerPlaceBlockEventListeners)
            ((PlayerPlaceBlockEventListenerInterface) playerPlaceBlockEventListener).onBlockPlace(player, packet);
    }
}
