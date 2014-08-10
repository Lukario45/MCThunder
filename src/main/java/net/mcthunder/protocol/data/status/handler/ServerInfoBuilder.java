package net.mcthunder.protocol.data.status.handler;

import net.mcthunder.packetlib.Session;
import net.mcthunder.protocol.data.status.ServerStatusInfo;

public interface ServerInfoBuilder {

    public ServerStatusInfo buildInfo(Session session);

}
