package net.mcthunder.events.listeners;

import net.mcthunder.api.Location;
import net.mcthunder.block.Block;
import net.mcthunder.block.Material;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerBreakBlockEventListenerInterface;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;

import static net.mcthunder.api.Utils.getDropped;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerBreakBlockEventListener implements PlayerBreakBlockEventListenerInterface {
    @Override
    public void onBlockBreak(Player player, ClientPlayerActionPacket packet) throws ClassNotFoundException {
        Block b = new Block(new Location(player.getWorld(), packet.getPosition()));
        if (player.getGameMode().equals(GameMode.SURVIVAL))
            //player.getWorld().loadEntity(new DroppedItem(b.getLocation(), new ItemStack(b.getType(), 1)));
            player.getWorld().loadEntity(getDropped(b));
        b.setType(Material.AIR);
    }
}