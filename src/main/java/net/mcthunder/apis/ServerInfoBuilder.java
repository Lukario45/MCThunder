package net.mcthunder.apis;

import net.mcthunder.game.essentials.ServerStatusInfo;
import net.mcthunder.packet.essentials.Session;

public abstract interface ServerInfoBuilder {
    public abstract ServerStatusInfo buildInfo(Session paramSession);
}