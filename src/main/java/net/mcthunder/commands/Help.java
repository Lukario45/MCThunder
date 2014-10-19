package net.mcthunder.commands;

import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
import net.mcthunder.apis.Player;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Help extends Command {//Ported by pup from Necessities
    public Help() {
        super("help", Arrays.asList(""), "Shows help messages", "/help <commandname>", 0, "command.help");
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
                search = args[0].toLowerCase();
            else
                page = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            if (!isLegal(args[1])) {
                player.sendMessage("Error: You must enter a valid help page.");
                return true;
            }
            search = args[0].toLowerCase();
            page = Integer.parseInt(args[1]);
        }
        if (args.length == 0 || page == 0)
            page = 1;
        int time = 0;
        String searched = "";
        String usage = "";
        for (String name : CommandRegistry.commands.keySet()) {
            Command c = CommandRegistry.commands.get(name);
            if(c == null)
                continue;
            if (name.toLowerCase().contains(search) || c.getInformation().toLowerCase().contains(search) || c.getAliases().contains(search) || search.equals("")) {
                helpList.add("/" + name + ": " + c.getInformation());
                if (name.equalsIgnoreCase(search) || (c.getAliases().contains(search) && !search.equals(""))) {
                    usage = /*helpList.add(*/"Usage: " + c.getArguments();//);
                    searched = "/" + name + ": " + c.getInformation();
                }
            }
        }
        Collections.sort(helpList);
        if(!usage.equals("") && !searched.equals("") && helpList.contains(searched))//should contain it but just in case
            helpList.add(helpList.indexOf(searched) + 1, usage);
        int rounder = 0;
        if (helpList.size() % 10 != 0)
            rounder = 1;
        int totalpages = (helpList.size() / 10) + rounder;
        if (page > totalpages) {
            player.sendMessage("Error: Input a number from 1 to " + Integer.toString(totalpages));
            return true;
        }
        if (search.equals(""))
            player.sendMessage(" ---- Help -- Page " + Integer.toString(page) + "/" + Integer.toString(totalpages) + " ---- ");
        else
            player.sendMessage(" ---- Help: " + search + " -- Page " + Integer.toString(page) + "/" + Integer.toString(totalpages) + " ---- ");
        page = page - 1;
        String message = getHelp(page, time, helpList);
        while (message != null) {
            player.sendMessage(message);
            time++;
            message = getHelp(page, time, helpList);
        }
        if (page + 1 < totalpages)
            player.sendMessage("Type /help " + Integer.toString(page + 2) + " to read the next page.");
        return true;
    }

    private String getHelp(int page, int time, ArrayList<String> helpList) {
        page *= 10;
        return (helpList.size() < time + page + 1 || time == 10) ? null : helpList.get(page + time);
    }

    private boolean isLegal(String toCheck) {
        try {
            Double.parseDouble(toCheck);
            return true;
        } catch (Exception ignored) { }
        return false;
    }
}