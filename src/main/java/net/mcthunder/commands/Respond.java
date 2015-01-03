package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

/**
 * Created by Kevin on 10/18/2014.
 */
public class Respond extends Command {
    public Respond() {
        super("respond", Arrays.asList("reply", "r"), "responds to the last persion who pmed you", "/r message", 0, "command.respond");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        Player p = player.getLastPmPerson();
        if (p == null) {
            player.sendMessage("&cNo one to respond to!");
            return true;
        }
        String[] wholeMessage = packet.getMessage().trim().split(" ");
        if (wholeMessage.length < 2)
            return false;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < wholeMessage.length; i++)
            sb.append(wholeMessage[i]).append(" ");
        String message = sb.toString().trim();
        player.sendMessage("&3[You &e---> &3" + p.getName() + "]:&r " + message);
        p.sendMessage("&3[" + player.getName() + " &e---> &3You]:&r " + message);
        p.setLastPmPerson(player);
        return true;
    }
}