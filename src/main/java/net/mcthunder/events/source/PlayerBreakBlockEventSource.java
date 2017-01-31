package net.mcthunder.events.source;

import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerBreakBlockEventListenerInterface;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerBreakBlockEventSource {
    private List<PlayerBreakBlockEventListenerInterface> playerBreakBlockEventListeners= new ArrayList<>();

    public synchronized void addEventListener(PlayerBreakBlockEventListenerInterface listener) {
        this.playerBreakBlockEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerBreakBlockEventListenerInterface listener) {
        this.playerBreakBlockEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Player player, ClientPlayerActionPacket packet) throws ClassNotFoundException {
        for (Object playerBreakBlockEventListener : playerBreakBlockEventListeners)
            ((PlayerBreakBlockEventListenerInterface) playerBreakBlockEventListener).onBlockBreak(player, packet);
    }
}
