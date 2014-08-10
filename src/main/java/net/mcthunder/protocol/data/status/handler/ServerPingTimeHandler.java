package net.mcthunder.protocol.data.status.handler;

import net.mcthunder.packetlib.Session;

public interface ServerPingTimeHandler {

    public void handle(Session session, long pingTime);

}
