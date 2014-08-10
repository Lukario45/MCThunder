package net.mcthunder.handlers;

import net.mcthunder.game.essentials.ServerStatusInfo;
import net.mcthunder.packet.essentials.Session;

public abstract interface ServerInfoHandler {
    public abstract void handle(Session paramSession, ServerStatusInfo paramServerStatusInfo);
}