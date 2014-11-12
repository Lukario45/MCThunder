package net.mcthunder.events.source;

import net.mcthunder.api.Player;
import net.mcthunder.events.PlayerLoggingInEvent;
import net.mcthunder.events.listeners.PlayerLoggingInEventListener;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.world.World;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.*;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerLoggingInEventSource {
    private List playerLoggingInEventListeners = new ArrayList();

    public synchronized void addEventListener(PlayerLoggingInEventListener listener) {
        playerLoggingInEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerLoggingInEventListener listener) {
        playerLoggingInEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Session session, ServerPlayerEntryListHandler entryListHandler, PlayerProfileHandler playerProfileHandler, World world) throws ClassNotFoundException {
        PlayerLoggingInEvent event = new PlayerLoggingInEvent(this);
        for (Object playerLoggingInEventListener : playerLoggingInEventListeners)
            ((PlayerLoggingInEventListener) playerLoggingInEventListener).onLogin(session, entryListHandler, playerProfileHandler);
    }
}
