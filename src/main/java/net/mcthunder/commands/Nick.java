package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

public class Nick extends Command {
    public Nick() {
        super("nick", Arrays.asList("nickname"), "Changes your nickname", "/nick <newname>", 9999, "command.nick");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().trim().split(" ");
        player.setDisplayName(wholeMessage.length == 1 ? null : wholeMessage[1]);
        player.sendMessage("Nickname updated.");
        return true;
    }
}