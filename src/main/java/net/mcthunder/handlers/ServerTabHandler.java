package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Player;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.packetlib.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/12/2014.
 */
public class ServerTabHandler {
    public void handleTabComplete(Session session, ClientTabCompletePacket packet) {
        String[] text = packet.getText().split(" ");
        String search = text[text.length - 1].toLowerCase();
        List<String> matches = new ArrayList<>();
        for (Player p : MCThunder.getPlayers())
            if (p.getName().toLowerCase().startsWith(search))
                matches.add(p.getName());
        if (!matches.isEmpty())
            session.send(new ServerTabCompletePacket(StringUtils.join(matches, "  ").split(" ")));
    }
}