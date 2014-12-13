package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Bot;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.block.Sign;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.Session;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerLoggingInEventListener implements net.mcthunder.interfaces.PlayerLoggingInEventListener {
    @Override
    public void onLogin(Session session) throws ClassNotFoundException {
        //TODO fire event with session
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        if (MCThunder.getPlayer(profile.getId()) != null)
            MCThunder.getPlayer(profile.getId()).disconnect("You logged in from another location!");
        Player player = new Player(session);
        MCThunder.addPlayer(player);
        CompoundTag c = (CompoundTag) MCThunder.getProfileHandler().getAttribute(player, "SpawnPosition");
        Location l = null;
        if (c != null)
            l = new Location(MCThunder.getWorld((String) c.get("World").getValue()), (double) c.get("X").getValue(), (double) c.get("Y").getValue(), (double) c.get("Z").getValue(), (float) c.get("Yaw").getValue(), (float) c.get("Pitch").getValue());
        player.setLocation((l == null || l.getWorld() == null) ? MCThunder.getWorld(MCThunder.getConfig().getWorldName()).getSpawnLocation() : l);
        player.sendPacket(new ServerJoinGamePacket(player.getEntityID(), player.getWorld().isHardcore(), player.getGameMode(), player.getWorld().getDimension(), player.getWorld().getDifficulty(), MCThunder.getConfig().getSlots(), player.getWorld().getWorldType(), false));
        tellConsole(LoggingLevel.INFO, String.format("User %s is connecting from %s:%s", player.getName(), session.getHost(), session.getPort()));
        MCThunder.addToPlayerEntryList(player);
        //Send World Data
        player.loadChunks(null);
        player.sendPacket(new ServerPlayerPositionRotationPacket(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
        player.sendPacket(new ServerSpawnPositionPacket(player.getLocation().getPosition()));
        MCThunder.broadcast("&7&o" + profile.getName() + " connected");
        MCThunder.getProfileHandler().checkPlayer(player);
        //StringTag test = (StringTag) playerProfileHandler.getAttribute(player,"test");
        // tellConsole(LoggingLevel.DEBUG,test.getValue());

        ServerSpawnPlayerPacket toAllPlayers = (ServerSpawnPlayerPacket) player.getPacket();
        for (Player player1 : MCThunder.getPlayers()) {
            if (!player1.getWorld().equals(player.getWorld()))
                continue;//Also will need to check if out of range ,_,
            if (!player1.getUniqueID().equals(player.getUniqueID())) {
                player1.sendPacket(toAllPlayers);
                player.sendPacket(player1.getPacket());
            }
        }
        for (Bot b : MCThunder.getBots()) {
            if (!b.getWorld().equals(player.getWorld()))
                continue;//Also will need to check if out of range ,_,
            player.sendPacket(b.getPacket());
        }
        for (Entity e : player.getWorld().getEntities())
            if (e.getPacket() != null)
                player.sendPacket(e.getPacket());
        for (Sign s : player.getWorld().getSigns())
            if (s.getPacket() != null)
                player.sendPacket(s.getPacket());
        player.toggleMoveable();
    }
}
