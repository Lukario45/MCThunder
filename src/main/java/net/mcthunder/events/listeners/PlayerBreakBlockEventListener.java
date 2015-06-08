package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.block.Block;
import net.mcthunder.block.Material;
import net.mcthunder.block.Sign;
import net.mcthunder.entity.Bot;
import net.mcthunder.entity.DroppedItem;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerBreakBlockEventListenerInterface;
import net.mcthunder.events.interfaces.PlayerLoggingInEventListenerInterface;
import net.mcthunder.inventory.ItemStack;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
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
public class PlayerBreakBlockEventListener implements PlayerBreakBlockEventListenerInterface {
    @Override
    public void onBlockBreak(Player player, ClientPlayerActionPacket packet) throws ClassNotFoundException {
        Block b = new Block(new Location(player.getWorld(), packet.getPosition()));
        if (player.getGameMode().equals(GameMode.SURVIVAL))
            player.getWorld().loadEntity(new DroppedItem(player.getLocation(), new ItemStack(b.getType(), 1)));
        b.setType(Material.AIR);
    }
}