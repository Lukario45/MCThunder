package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 11/5/2014.
 */
public class Teleport extends Command {
    public Teleport() {
        super("teleport", Arrays.asList("tp"), "teleports players", "/tp player toplayer", 9999, "commands.teleport");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length < 2)
            return false;
        Player toSend = MCThunder.getPlayer(wholeMessage[1]);
        Player sendTo = MCThunder.getPlayer(wholeMessage[2]);
        if (toSend == null)
            player.sendMessage("&cThat player is not online!");
        else {
            if (sendTo == null) {
                sendTo = toSend;
                 toSend = player;
            }
            toSend.teleport(sendTo.getLocation());
        }
        return true;
    }
}