package net.mcthunder.interfaces;

import net.mcthunder.api.Player;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.world.World;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Kevin on 11/12/2014.
 */
public interface PlayerLoggingInEventListener {
    public void onLogin(Session session, Server server, HashMap<UUID, Player> playerHashMap, ServerPlayerEntryListHandler entryListHandler, PlayerProfileHandler playerProfileHandler, World world) throws ClassNotFoundException;
}
