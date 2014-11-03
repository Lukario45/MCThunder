package net.mcthunder.handlers;

import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/12/2014.
 */
public class ServerTabHandler {
    public void handleTabComplete(Server server, Session session, ClientTabCompletePacket packet) {
        String[] text = packet.getText().split(" ");
        String search = text[text.length - 1];
        List<Session> sessions = server.getSessions();
        List<String> matches = new ArrayList<>();

        for (Session s : sessions) {
            String name = s.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getName();
            if (name.toLowerCase().startsWith(search.toLowerCase()))
                matches.add(name);
        }
        if (!matches.isEmpty())
            session.send(new ServerTabCompletePacket(StringUtils.join(matches, "  ").split(" ")));
    }
}