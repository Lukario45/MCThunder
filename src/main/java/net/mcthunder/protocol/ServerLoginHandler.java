package net.mcthunder.protocol;

import org.spacehq.packetlib.Session;

public interface ServerLoginHandler {

    public void loggedIn(Session session);

}
