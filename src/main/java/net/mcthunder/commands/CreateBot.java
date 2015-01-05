package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import net.mcthunder.tests.TestBot;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

public class CreateBot extends Command {
    public CreateBot() {
        super("createbot", Arrays.asList("bot"), "Creates a bot. Temporary command", "/createbot <name>", 9999, "command.createbot");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] wholeMessage = packet.getMessage().trim().split(" ");
        if (wholeMessage.length < 2)
            return false;
        TestBot b = new TestBot(wholeMessage[1]);
        MCThunder.loadBot(b);
        b.spawn(player.getLocation());
        b.setCape("http://textures.minecraft.net/texture/3f688e0e699b3d9fe448b5bb50a3a288f9c589762b3dae8308842122dcb81");
        return true;
    }
}