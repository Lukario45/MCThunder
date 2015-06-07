package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;

import java.util.Arrays;

public class Gamemode extends Command {
    public Gamemode() {
        super("gamemode", Arrays.asList("gm"), "Changes your gamemode", "/gamemode <GameMode>", 9999, "core.gamemode");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        GameMode gm;
        switch (args[0].toLowerCase()) {
            case "creative": case "c": case "1":
                gm = GameMode.CREATIVE;
                break;
            case "adventure": case "a": case "2":
                gm = GameMode.ADVENTURE;
                break;
            case "spectator": case "sp": case "3":
                gm = GameMode.SPECTATOR;
                break;
            default://case "survival": case "s": case "0":
                gm = GameMode.SURVIVAL;
                break;
        }
        if (!gm.equals(player.getGameMode())) {
            player.setGameMode(gm);
            player.sendMessage("Gamemode updated.");
        }
        return true;
    }
}