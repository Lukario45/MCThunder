package net.mcthunder.rankmanager.listeners;

import com.Lukario45.NBTFile.Utilities;
import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.CommandRegistry;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

import static net.mcthunder.api.Utils.tellConsole;

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
        String playerRank = Utilities.getFromCompound((CompoundTag) MCThunder.getProfileHandler().getAttribute(player, "RankManager"), "RankName").getValue().toString();
        String command = packet.getMessage().split(" ")[0].split("/")[1].toLowerCase();
        Command cmd = CommandRegistry.getCommand(command);
        if (cmd == null) {
            player.sendMessage("&4Unknown Command!");
            return;
        }


        if (CommandRegistry.getCommand("rankmanager:" + command) != null)//Overide default commands with rankmanager ones
            cmd = CommandRegistry.getCommand("rankmanager:" + command);


        if (MCThunder.getRankManager().getPlayerRankMap().get(player).containsSpecialPerm(cmd.getPermissionNode()) == 0){
            if (!cmd.execute(player, packet.getMessage().contains(" ") ? packet.getMessage().trim().substring(packet.getMessage().trim().indexOf(" ")).trim().split(" ") : new String[0])){
                player.sendMessage("&4" + cmd.getArguments());
            }

        } else if(MCThunder.getRankManager().getPlayerRankMap().get(player).containsSpecialPerm(cmd.getPermissionNode()) == 1){
            MCThunder.getRankManager().deny(player, cmd);

        } else if(MCThunder.getRankManager().getPlayerRankMap().get(player).containsSpecialPerm(cmd.getPermissionNode()) == 2){

            if (MCThunder.getRankManager().getPlayerRankMap().get(player).getRankLevel() >= cmd.getRankPoints()){
                if (!cmd.execute(player, packet.getMessage().contains(" ") ? packet.getMessage().trim().substring(packet.getMessage().trim().indexOf(" ")).trim().split(" ") : new String[0])){
                    player.sendMessage("&4" + cmd.getArguments());
                }
                tellConsole(LoggingLevel.COMMAND, "Player " + player.getDisplayName() + " " + packet.getMessage());
            } else {
                MCThunder.getRankManager().deny(player, cmd);
            }

            }

    }
}