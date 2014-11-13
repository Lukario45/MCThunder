package net.mcthunder.rankmanager.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Player;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
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
    public void onLogin(Session session, ServerPlayerEntryListHandler entryListHandler, PlayerProfileHandler playerProfileHandler) throws ClassNotFoundException {
        Player player = MCThunder.getPlayer(session.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
        Map<String, Tag> map = new HashMap<>();
        StringTag stringTag = new StringTag("RankName", "Test");
        map.put(stringTag.getName(), stringTag);
        CompoundTag c = new CompoundTag("RankManager", map);
        playerProfileHandler.addCompundAttribute(player, c);
    }
}