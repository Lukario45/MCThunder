package net.mcthunder.handlers;

import net.mcthunder.api.Config;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntryAction;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/12/2014.
 */
public class ServerPlayerEntryListHandler {
    Config conf;
    List<PlayerListEntry> playerListEntries;

    public static void deleteFromPlayerEntryList(Server server, Session session) {
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        List<Session> sessions = server.getSessions();
        PlayerListEntry oldPlayer = new PlayerListEntry(profile);
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[]{oldPlayer});
        for (Session s : sessions)
            s.send(serverPlayerListEntryPacket);
    }

    public void addToPlayerEntryList(Server server, Session session, GameMode g) {
        conf = new Config();
        playerListEntries = new ArrayList<>();
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        List<Session> sessions = server.getSessions();
        int ping = session.getFlag(ProtocolConstants.PING_KEY);
        PlayerListEntry newPlayer = new PlayerListEntry(profile, g, ping, Message.fromString(profile.getName()));
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{newPlayer});
        for (Session s : sessions)
            s.send(serverPlayerListEntryPacket);
        for (Session s : sessions)
            playerListEntries.add(new PlayerListEntry(s.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY), g, ping, Message.fromString(s.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getName())));
        PlayerListEntry[] playerListEntriesArray = playerListEntries.toArray(new PlayerListEntry[playerListEntries.size()]);
        session.send(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, playerListEntriesArray));
    }
}