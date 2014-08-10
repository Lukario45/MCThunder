package net.mcthunder.protocol.data.status.handler;

import net.mcthunder.protocol.data.status.ServerStatusInfo;
import org.spacehq.packetlib.Session;


public interface ServerInfoHandler {

    public void handle(Session session, ServerStatusInfo info);

}
