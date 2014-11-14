package net.mcthunder.rankmanager.commands;

import net.mcthunder.api.Command;
import net.mcthunder.api.CommandRegistry;
import net.mcthunder.api.Player;
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
        if (packet.getMessage().contains(" "))
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
                player.sendMessage("&4Error: You must enter a valid help page.");
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
        for (String name : CommandRegistry.getCommands().keySet()) {
            Command c = CommandRegistry.getCommand(name);
            if (c == null)
                continue;
            if (name.toLowerCase().contains(search) || c.getInformation().toLowerCase().contains(search) || c.getAliases().contains(search) || search.equals("")) {
                helpList.add("&3/" + name + ": &e" + c.getInformation());
                if (name.equalsIgnoreCase(search) || (c.getAliases().contains(search) && !search.equals(""))) {
                    usage = "&eUsage: &3" + c.getArguments();
                    searched = "&3/" + name + ": &e" + c.getInformation();
                }
            }
        }
        Collections.sort(helpList);
        if (!usage.equals("") && !searched.equals("") && helpList.contains(searched))//should contain it but just in case
            helpList.add(helpList.indexOf(searched) + 1, usage);
        int totalpages = (helpList.size() / 10) + (helpList.size() % 10 != 0 ? 1 : 0);
        if (page > totalpages) {
            player.sendMessage("&cError: Input a number from 1 to " + Integer.toString(totalpages));
            return true;
        }
        player.sendMessage("&e---- &3Help" + (search.equals("") ? "" : ": " + search) + " &e-- &3Page&4 " + Integer.toString(page) + "&3/&4" + Integer.toString(totalpages) + " &e----");
        page -= 1;
        String message = getHelp(page, time, helpList);
        while (message != null) {
            player.sendMessage(message);
            message = getHelp(page, ++time, helpList);
        }
        if (page + 1 < totalpages)
            player.sendMessage("&eType &3/help&4 " + Integer.toString(page + 2) + "&e to read the next page.");
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
        } catch (Exception ignored) {
        }
        return false;
    }
}