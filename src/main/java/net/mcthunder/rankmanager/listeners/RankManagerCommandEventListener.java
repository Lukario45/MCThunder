package net.mcthunder.rankmanager.listeners;

import com.Lukario45.NBTFile.Utilities;
import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.CommandRegistry;
import net.mcthunder.entity.Player;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

/**
 * Created by Kevin on 10/13/2014.
 */
public class RankManagerCommandEventListener implements net.mcthunder.interfaces.PlayerCommandEventListener {


    @Override
    public boolean removeDefaultListener() {
        return true;
    }

    @Override
    public void onCommand(Player player, ClientChatPacket packet) {

        try {
             String playerRank = Utilities.getFromCompound((CompoundTag) MCThunder.getProfileHandler().getAttribute(player, "RankManager"), "RankName").getValue().toString();
            String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);

            if (MCThunder.getRankManager().getCommandLevelFromRank(playerRank) >= CommandRegistry.getCommand(command,"net.mcthunder.commands.").getRankPoints()){
                 Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");
                if (!cmd.execute(player, packet.getMessage().contains(" ") ? packet.getMessage().trim().substring(packet.getMessage().trim().indexOf(" ")).trim().split(" ") : new String[0]))
                    player.sendMessage("&4" + cmd.getArguments());
            } else {
                MCThunder.getRankManager().deny(player,CommandRegistry.getCommand(command, "net.mcthunder.commands."));
            }

            //commandHandler.handlePlayerCommand(player, packet);
        } catch (NullPointerException e) {
            player.sendMessage("&4Unknown Command!");
        }
    }
}