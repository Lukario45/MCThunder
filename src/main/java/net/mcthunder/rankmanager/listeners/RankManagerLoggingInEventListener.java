package net.mcthunder.rankmanager.listeners;

import com.Lukario45.NBTFile.Utilities;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import net.mcthunder.MCThunder;
import net.mcthunder.entity.Player;
import net.mcthunder.rankmanager.PlayerRank;
import net.mcthunder.rankmanager.RankManager;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.opennbt.tag.builtin.Tag;
import org.spacehq.opennbt.tag.builtin.custom.StringArrayTag;
import org.spacehq.packetlib.Session;

import java.util.*;

/**
 * Created by Kevin on 11/12/2014.
 */
public class RankManagerLoggingInEventListener implements net.mcthunder.interfaces.PlayerLoggingInEventListener {
    @Override
    public void onLogin(Session session) throws ClassNotFoundException {
        Player player = MCThunder.getPlayer(session.<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
        if (!MCThunder.getProfileHandler().tagExists(player, "RankManager")){
            Map<String, Tag> map = new HashMap<>();
            map.put("RankName", new StringTag("RankName", MCThunder.getRankManager().getDefaultRank()));
            map.put("SpecialPerms", new ListTag("SpecialPerms", new ArrayList()));
            MCThunder.getProfileHandler().addAttribute(player, new CompoundTag("RankManager", map));
        }
        CompoundTag rankManager = (CompoundTag) MCThunder.getProfileHandler().getAttribute(player,"RankManager");
        List<Tag> nodes = (List) Utilities.getFromCompound(rankManager,"SpecialPerms").getValue();
        MCThunder.getRankManager().getPlayerRankMap().put(player, new PlayerRank(Utilities.getFromCompound(rankManager, "RankName").getValue().toString()));
        for (Tag t : nodes)
            MCThunder.getRankManager().getPlayerRankMap().get(player).addSpecialPerm(t.getValue().toString());
    }
}