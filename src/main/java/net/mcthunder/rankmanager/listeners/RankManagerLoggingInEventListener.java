package net.mcthunder.rankmanager.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Player;
import net.mcthunder.rankmanager.RankManager;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.packetlib.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 11/12/2014.
 */
public class RankManagerLoggingInEventListener implements net.mcthunder.interfaces.PlayerLoggingInEventListener {
    @Override
    public void onLogin(Session session) throws ClassNotFoundException {
        RankManager manager = MCThunder.getRankManager();
        Player player = MCThunder.getPlayer(session.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
        Map<String, Tag> map = new HashMap<>();
        map.put("RankName", new StringTag("RankName", manager.getDefaultRank() ));
        if (!MCThunder.getProfileHandler().tagExists(player, "RankManager"))
            MCThunder.getProfileHandler().addAttribute(player, new CompoundTag("RankManager", map));
    }
}
