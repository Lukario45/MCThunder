package net.mcthunder.protocol.data.status.handler;

import net.mcthunder.packetlib.Session;
import net.mcthunder.protocol.data.status.ServerStatusInfo;


public interface ServerInfoHandler {

    public void handle(Session session, ServerStatusInfo info);

}
