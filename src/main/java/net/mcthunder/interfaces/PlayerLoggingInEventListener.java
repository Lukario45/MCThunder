package net.mcthunder.interfaces;

import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 11/12/2014.
 */
public interface PlayerLoggingInEventListener {
    public void onLogin(Session session, ServerPlayerEntryListHandler entryListHandler, PlayerProfileHandler playerProfileHandler) throws ClassNotFoundException;
}
