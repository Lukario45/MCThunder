package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Bot;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntryAction;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/12/2014.
 */
public class ServerPlayerEntryListHandler {
    public void removeFromList(Player player) {
        ServerPlayerListEntryPacket serverPlayerListExitPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[]{new PlayerListEntry(player.getGameProfile())});
        for (Player p : MCThunder.getPlayers())
            p.sendPacket(serverPlayerListExitPacket);
    }

    public void addToList(Player player) {
        List<PlayerListEntry> playerListEntries = new ArrayList<>();
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{player.getListEntry()});
        for (Player p : MCThunder.getPlayers()) {
            p.sendPacket(serverPlayerListEntryPacket);
            playerListEntries.add(p.getListEntry());
        }
        for (Bot b : MCThunder.getBots())
            playerListEntries.add(b.getListEntry());
        player.sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, playerListEntries.toArray(new PlayerListEntry[playerListEntries.size()])));
    }

    public void refresh(Player player) {
        ServerPlayerListEntryPacket serverPlayerListExitPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[]{new PlayerListEntry(player.getGameProfile())});
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{player.getListEntry()});
        for (Player p : MCThunder.getPlayers()) {
            p.sendPacket(serverPlayerListExitPacket);
            p.sendPacket(serverPlayerListEntryPacket);
        }
    }

    public void removeFromList(Bot bot) {
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[]{new PlayerListEntry(bot.getGameProfile())});
        for (Player p : MCThunder.getPlayers())
            p.sendPacket(serverPlayerListEntryPacket);
    }

    public void addToList(Bot bot) {
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{bot.getListEntry()});
        for (Player p : MCThunder.getPlayers())
            p.sendPacket(serverPlayerListEntryPacket);
    }

    public void refresh(Bot bot) {
        ServerPlayerListEntryPacket serverPlayerListExitPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[]{new PlayerListEntry(bot.getGameProfile())});
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, new PlayerListEntry[]{bot.getListEntry()});
        for (Player p : MCThunder.getPlayers()) {
            p.sendPacket(serverPlayerListExitPacket);
            p.sendPacket(serverPlayerListEntryPacket);
        }
    }
}