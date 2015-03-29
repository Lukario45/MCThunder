package net.mcthunder.events.listeners;

import net.mcthunder.api.Command;
import net.mcthunder.api.CommandRegistry;
import net.mcthunder.entity.Player;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerCommandEventListener implements net.mcthunder.interfaces.PlayerCommandEventListener {


    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onCommand(Player player, ClientChatPacket packet) {

        try {
            String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);
            Command cmd = CommandRegistry.getCommand(command, "net.mcthunder.commands.");

            if (!cmd.execute(player, packet.getMessage().contains(" ") ? packet.getMessage().trim().substring(packet.getMessage().trim().indexOf(" ")).trim().split(" ") : new String[0]))
                player.sendMessage("&4" + cmd.getArguments());
        } catch (NullPointerException e) {
            player.sendMessage("&4Unknown Command!");
        }
    }
}