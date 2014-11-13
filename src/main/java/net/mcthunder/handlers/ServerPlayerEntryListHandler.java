package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntry;
import org.spacehq.mc.protocol.data.game.values.PlayerListEntryAction;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 10/12/2014.
 */
public class ServerPlayerEntryListHandler {

    public void deleteFromPlayerEntryList(Player player) {
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.REMOVE_PLAYER, new PlayerListEntry[]{new PlayerListEntry(player.getGameProfile())});
        for (Player p : MCThunder.getPlayers())
            p.sendPacket(serverPlayerListEntryPacket);
    }

    public void addToPlayerEntryList(Player player) {
        List<PlayerListEntry> playerListEntries = new ArrayList<>();
        ServerPlayerListEntryPacket serverPlayerListEntryPacket = new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER,
                new PlayerListEntry[]{new PlayerListEntry(player.getGameProfile(), player.getGameMode(), player.getPing(), Message.fromString(player.getName()))});
        for (Player p : MCThunder.getPlayers()) {
            p.sendPacket(serverPlayerListEntryPacket);
            playerListEntries.add(new PlayerListEntry(p.getGameProfile(), p.getGameMode(), player.getPing(), Message.fromString(p.getName())));
        }
        player.sendPacket(new ServerPlayerListEntryPacket(PlayerListEntryAction.ADD_PLAYER, playerListEntries.toArray(new PlayerListEntry[playerListEntries.size()])));
    }
}