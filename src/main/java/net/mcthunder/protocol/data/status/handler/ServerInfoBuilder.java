package net.mcthunder.protocol.data.status.handler;

import net.mcthunder.protocol.data.status.ServerStatusInfo;
import org.spacehq.packetlib.Session;

public interface ServerInfoBuilder {

    public ServerStatusInfo buildInfo(Session session);

}
