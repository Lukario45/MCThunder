package net.mcthunder.commands;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;

import java.util.Arrays;

public class Give extends Command {
    public Give() {
        super("give", "Gives a player an item", "/give <player> <item> <amount>", 9999, "command.give");
    }

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("&4Error: You need to enter an item and a player to give that item to.");
            return true;
        }
        Player t = MCThunder.getPlayer(args[0]);
        if (t == null) {
            player.sendMessage("&4Error: Invalid player.");
            return true;
        }
        Material item;
        short data = 0;
        if (args[1].contains(":")) {
            if (isLegal(args[1].split(":")[1]))
                data = Short.parseShort(args[1].split(":")[1]);
            args[1] = args[1].split(":")[0];
        }
        if (isLegal(args[1]))
            item = Material.fromData(Integer.parseInt(args[1]), data);
        else
            item = Material.fromData(args[1], data);
        if (item == null) {
            player.sendMessage("&4Error: That item does not exist.");
            return true;
        }
        if (args.length == 2) {
            t.getInventory().add(new ItemStack(item, 64));
            player.sendMessage("&6Giving &c64 " + item.plural(64) + " to " + t.getDisplayName() + "&6.");
            return true;
        }
        if (!isLegal(args[2])) {
            player.sendMessage("&4Error: Illegal amount the amount must be numeric.");
            return true;
        }
        int amount = Integer.parseInt(args[2]);
        if (args.length > 3 && isLegal(args[3]))
            data = Short.parseShort(args[3]);
        t.getInventory().add(new ItemStack(Material.fromData(item, data), amount));
        player.sendMessage("&6Giving &c" + amount + " " + item.plural(amount) + " to " + t.getDisplayName() + "&6.");
        return true;
    }

    private boolean isLegal(String test) {
        try {
            Integer.parseInt(test);
            return true;
        } catch (Exception ignored) { }
        return  false;
    }
}