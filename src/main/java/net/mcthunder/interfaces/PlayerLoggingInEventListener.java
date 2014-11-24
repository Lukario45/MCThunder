package net.mcthunder.interfaces;

import org.spacehq.packetlib.Session;

/**
 * Created by Kevin on 11/12/2014.
 */
public interface PlayerLoggingInEventListener {
    public void onLogin(Session session) throws ClassNotFoundException;
}
