package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

public class SetSkin extends Command {
    public SetSkin() {
        super("setskin", Arrays.asList("skin"), "Sets your skin to another players", "/setskin <optional player>", 9999, "command.setskin");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().split(" ");
        if (wholeMessage.length < 2)
            player.removeSkin();
        else
            player.setSkin(wholeMessage[1]);
        player.sendMessage("Skin updated.");
        return true;
    }
}