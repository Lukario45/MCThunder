package net.mcthunder.events.listeners;

import net.mcthunder.api.Command;
import net.mcthunder.api.CommandRegistry;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerCommandEventListenerInterface;
import org.apache.commons.lang.StringUtils;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerCommandEventListener implements PlayerCommandEventListenerInterface {


    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onCommand(Player player, ClientChatPacket packet) {
        String command = StringUtils.lowerCase(packet.getMessage().split(" ")[0].split("/")[1]);
        Command cmd = CommandRegistry.getCommand(command);
        if (cmd == null) {
            player.sendMessage("&4Unknown Command!");
            return;
        }

        if (!cmd.execute(player, packet.getMessage().contains(" ") ? packet.getMessage().trim().substring(packet.getMessage().trim().indexOf(" ")).trim().split(" ") : new String[0]))
            player.sendMessage("&4" + cmd.getArguments());
        tellConsole(LoggingLevel.COMMAND, "Player " + player.getDisplayName() + " " + packet.getMessage());
    }
}