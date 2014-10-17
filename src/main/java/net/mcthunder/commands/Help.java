package net.mcthunder.commands;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.data.message.ChatColor;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.ArrayList;

public class Help extends Command {//Ported by pup from Necessities
    public Help() {
        super("Help", "help", "shows help messages", 9999, "command.help");
    }

    @Override
    public boolean execute(Player player, ClientChatPacket packet) {
        String[] args = new String[0];
        if(packet.getMessage().contains(" "))
            args = packet.getMessage().substring(packet.getMessage().indexOf(" ")).trim().split(" ");
        ArrayList<String> helpList = new ArrayList<>();
        int page = 0;
        String search = "";
        if (args.length == 1) {
            if (!isLegal(args[0]))
                search = args[0];
            else
                page = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            if (!isLegal(args[1])) {
                player.sendMessageToPlayer("Error: You must enter a valid help page.");
                return true;
            }
            search = args[0];
            page = Integer.parseInt(args[1]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        for (String name : CommandRegistry.commands.keySet())
            if (name.contains(search) || CommandRegistry.commands.get(name).getInformation().contains(search) || search.equals(""))
                helpList.add("/" + name + ": " + CommandRegistry.commands.get(name).getInformation());
        int rounder = 0;
        if (helpList.size() % 10 != 0)
            rounder = 1;
        int totalpages = (helpList.size() / 10) + rounder;
        if (page > totalpages) {
            player.sendMessageToPlayer("Error: Input a number from 1 to " + Integer.toString(totalpages));
            return true;
        }
        if (search.equals(""))
            player.sendMessageToPlayer(" ---- Help -- Page " + Integer.toString(page) + "/" + Integer.toString(totalpages) + " ---- ");
        else
            player.sendMessageToPlayer(" ---- Help: " + search + " -- Page " + Integer.toString(page) + "/" + Integer.toString(totalpages) + " ---- ");
        page = page - 1;
        String message = getHelp(page, time, helpList);
        while (message != null) {
            player.sendMessageToPlayer(message);
            time++;
            message = getHelp(page, time, helpList);
        }
        if (page + 1 < totalpages)
            player.sendMessageToPlayer("Type " + "/help " + Integer.toString(page + 2) + " to read the next page.");
        return true;
    }

    private String getHelp(int page, int time, ArrayList<String> helpList) {
        page *= 10;
        if (helpList.size() < time + page + 1 || time == 10)
            return null;
        return helpList.get(page + time);
    }

    private boolean isLegal(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception ignored) { }
        return false;
    }
}