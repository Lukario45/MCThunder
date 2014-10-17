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
        //ClientTabCompletePacket packet = event.getPacket();

        String[] text = packet.getText().split(" ");
        String word = text[text.length - 1];
        List<Session> sessions = server.getSessions();
        List<String> names = new ArrayList<String>();
        List<String> matches = new ArrayList<String>();

        for (Session s : sessions) {
            GameProfile p = s.getFlag(ProtocolConstants.PROFILE_KEY);
            String name = p.getName();
            if (name.toLowerCase().startsWith(word.toLowerCase()))
                matches.add(name);
        }
        String[] matchArray = StringUtils.join(matches, "  ").split(" ");
        ServerTabCompletePacket serverTabCompletePacket = new ServerTabCompletePacket(matchArray);
        if (matches.size() != 0) {
            //chatHandler.sendPrivateMessage(event.getSession(), StringUtils.join(matches, ", "));
            session.send(serverTabCompletePacket);
        }
    }
}