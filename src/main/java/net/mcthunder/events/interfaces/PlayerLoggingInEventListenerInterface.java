package net.mcthunder.events.interfaces;

import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 11/12/2014.
 */
public interface PlayerLoggingInEventListenerInterface {
    public void onLogin(Session session) throws ClassNotFoundException;
}
