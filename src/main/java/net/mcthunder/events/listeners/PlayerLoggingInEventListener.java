package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.world.World;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;

import java.util.HashMap;
import java.util.UUID;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerLoggingInEventListener implements net.mcthunder.interfaces.PlayerLoggingInEventListener {
    @Override
    public void onLogin(Session session, Server server, HashMap<UUID, Player> playerHashMap, ServerPlayerEntryListHandler entryListHandler, PlayerProfileHandler playerProfileHandler, World world) throws ClassNotFoundException {
        //TODO fire event with session server playerhashmap entryListGabdker playerProfileHandler
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        if (playerHashMap.containsKey(profile.getId()))
            MCThunder.getPlayer(profile.getId()).disconnect("You logged in from another location!");

        int entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        EntityMetadata metadata = new EntityMetadata(2, MetadataType.STRING, profile.getName());
        Player player = new Player(session, entityID, metadata);
        playerHashMap.put(profile.getId(), player);
        MCThunder.addPlayer(player);
        CompoundTag c = (CompoundTag) playerProfileHandler.getAttribute(player, "SpawnPosition");
        Location l = null;
        if (c != null)
            l = new Location(MCThunder.getWorld((String) c.get("World").getValue()), (double) c.get("X").getValue(), (double) c.get("Y").getValue(), (double) c.get("Z").getValue(), (float) c.get("Yaw").getValue(), (float) c.get("Pitch").getValue());
        player.setLocation(l == null ? world.getSpawnLocation() : l);
        player.sendPacket(new ServerJoinGamePacket(player.getEntityID(), player.getWorld().isHardcore(), player.getGameMode(), player.getWorld().getDimension(), player.getWorld().getDifficulty(), MCThunder.getConfig().getSlots(), player.getWorld().getWorldType(), false));
        tellConsole(LoggingLevel.INFO, String.format("User %s is connecting from %s:%s", player.getGameProfile().getName(), session.getHost(), session.getPort()));
        entryListHandler.addToPlayerEntryList(player);
        MCThunder.updateEntryList(entryListHandler);
        //Send World Data
        player.loadChunks(null);
        player.sendPacket(new ServerPlayerPositionRotationPacket(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
        player.sendPacket(new ServerSpawnPositionPacket(new Position((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ())));
        MCThunder.broadcast("&7&o" + profile.getName() + " connected");
        playerProfileHandler.checkPlayer(player);
        //StringTag test = (StringTag) playerProfileHandler.getAttribute(player,"test");
        // tellConsole(LoggingLevel.DEBUG,test.getValue());


        ServerSpawnPlayerPacket toAllPlayers = new ServerSpawnPlayerPacket(player.getEntityID(), player.getUniqueID(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), player.getHeldItem().getId(), player.getMetadata().getMetadataArray());
        for (Player player1 : MCThunder.getPlayers()) {
            if (!player1.getWorld().equals(player.getWorld()))
                continue;//Also will need to check if out of range ,_,
            ServerSpawnPlayerPacket toNewPlayer = new ServerSpawnPlayerPacket(player1.getEntityID(), player1.getGameProfile().getId(), player1.getLocation().getX(), player1.getLocation().getY(), player1.getLocation().getZ(), player1.getLocation().getYaw(), player1.getLocation().getPitch(), player1.getHeldItem().getId(), player1.getMetadata().getMetadataArray());
            if (!player1.getUniqueID().equals(player.getUniqueID())) {
                player1.sendPacket(toAllPlayers);
                player.sendPacket(toNewPlayer);
            }
        }
        player.toggleMoveable();

    }
}
