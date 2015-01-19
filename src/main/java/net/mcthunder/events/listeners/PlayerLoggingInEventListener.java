package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.block.Sign;
import net.mcthunder.entity.Bot;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerLoggingInEventListener implements net.mcthunder.interfaces.PlayerLoggingInEventListener {
    @Override
    public void onLogin(Session session) throws ClassNotFoundException {
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        if (MCThunder.getPlayer(profile.getId()) != null)
            MCThunder.getPlayer(profile.getId()).disconnect("You logged in from another location!");
        Player player = new Player(session);
        MCThunder.addPlayer(player);
        player.sendPacket(new ServerJoinGamePacket(player.getEntityID(), player.getWorld().isHardcore(), player.getGameMode(), player.getWorld().getDimension(), player.getWorld().getDifficulty(), MCThunder.getConfig().getSlots(), player.getWorld().getWorldType(), false));
        tellConsole(LoggingLevel.INFO, String.format("User %s is connecting from %s:%s", player.getName(), session.getHost(), session.getPort()));
        MCThunder.addToPlayerEntryList(player);
        //Send World Data
        player.loadChunks(null);
        player.sendPacket(new ServerPlayerPositionRotationPacket(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
        player.sendPacket(new ServerSpawnPositionPacket(player.getLocation().getPosition()));
        MCThunder.broadcast("&7&o" + profile.getName() + " connected");
        ServerSpawnPlayerPacket toAllPlayers = (ServerSpawnPlayerPacket) player.getPacket();
        for (Player player1 : MCThunder.getPlayers())
            if (player1.getWorld().equals(player.getWorld()) && player1.isColumnLoaded(player.getChunk()) && !player1.getUniqueID().equals(player.getUniqueID())) {
                player1.sendPacket(toAllPlayers);
                player.sendPacket(player1.getPacket());
            }
        for (Bot b : MCThunder.getBots())
            if (b.getWorld().equals(player.getWorld()) && player.isColumnLoaded(b.getChunk()))
                for (Packet p : b.getPackets())
                    player.sendPacket(p);
        for (Entity e : player.getWorld().getEntities())
            if(player.isColumnLoaded(e.getChunk()))
                for (Packet packet : e.getPackets())
                    player.sendPacket(packet);
        for (Sign s : player.getWorld().getSigns())
            player.sendPacket(s.getPacket());
        player.updateInventory();
        player.toggleMoveable();
    }
}