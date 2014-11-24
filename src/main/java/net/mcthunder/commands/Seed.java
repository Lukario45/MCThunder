package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.Arrays;

public class Seed extends Command {
    public Seed() {
        super("seed", Arrays.asList(""), "Retrieves the world seed", "/seed", 9999, "command.seed");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        player.sendMessage("Seed: " + player.getWorld().getSeed());
        return true;
    }
}