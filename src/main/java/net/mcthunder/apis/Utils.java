package net.mcthunder.apis;

import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntryAction;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kevin on 8/9/2014.
 */
public class Utils {
    static Config conf;
    static List<PlayerListEntry> playerListEntries;

    public static void updatePlayerEntryList(Server server, Session session) {
        conf = new Config();
        playerListEntries = new ArrayList<PlayerListEntry>();

        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        List<Session> sessions = server.getSessions();
        int ping = session.getFlag(ProtocolConstants.PING_KEY);
        PlayerListEntry newPlayer = new PlayerListEntry(profile, GameMode.CREATIVE, ping, Message.fromString(profile.getName()));
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{newPlayer});
        for (Session s : sessions) {
            s.send(serverPlayerListEntryPacket);
        }
        for (Session s : sessions) {
            // int entrySize = 0;
            //int allPing = s.getFlag(ProtocolConstants.PING_KEY);
            PlayerListEntry p = new PlayerListEntry(s.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY), GameMode.CREATIVE, ping, Message.fromString(s.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getName()));
            playerListEntries.add(p);
            //entrySize++;
        }
        PlayerListEntry[] playerListEntriesArray = playerListEntries.toArray(new PlayerListEntry[playerListEntries.size()]);
        ServerPlayerListEntryPacket packet = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, playerListEntriesArray);
        session.send(packet);


    }


    public static void tellConsole(String type, String message) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(date + " [" + type + "]: " + message);
    }

}
