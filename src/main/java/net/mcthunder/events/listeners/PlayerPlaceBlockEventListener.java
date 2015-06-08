package net.mcthunder.events.listeners;

import net.mcthunder.api.Direction;
import net.mcthunder.api.Location;
import net.mcthunder.block.Block;
import net.mcthunder.block.Material;
import net.mcthunder.entity.DroppedItem;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.EntityType;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerBreakBlockEventListenerInterface;
import net.mcthunder.events.interfaces.PlayerPlaceBlockEventListenerInterface;
import net.mcthunder.inventory.*;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerPlaceBlockEventListener implements PlayerPlaceBlockEventListenerInterface {
    @Override
    public void onBlockPlace(Player player, ClientPlayerPlaceBlockPacket packet) throws ClassNotFoundException {
        Position position = packet.getPosition();
        ItemStack heldItem = packet.getHeldItem() == null ? null : new ItemStack(Material.fromData(packet.getHeldItem().getId(),
                (short) packet.getHeldItem().getData()), packet.getHeldItem().getAmount());
        if ((position.getY() >> 4) < 0)
            return;
        Block b = new Block(new Location(player.getWorld(), position));
        if (!player.isSneaking()) {
            Inventory inv = b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) ?
                    player.getWorld().getChest(b.getLocation()).getInventory() : b.getType().equals(Material.ENDER_CHEST) ?
                    player.getEnderChest() : b.getType().equals(Material.CRAFTING_TABLE) ? new CraftingInventory("Crafting") :
                    b.getType().getParent().equals(Material.HOPPER) ? new HopperInventory("Hopper") :
                            b.getType().equals(Material.BEACON) ? new BeaconInventory("Beacon") :
                                    b.getType().getParent().equals(Material.ANVIL) ? new AnvilInventory("Anvil") :
                                            b.getType().equals(Material.BREWING_STAND_BLOCK) ? new BrewingStandInventory("BrewingStand") :
                                                    b.getType().equals(Material.DISPENSER) ? new DispenserInventory("Dispenser") :
                                                            b.getType().equals(Material.DROPPER) ? new DropperInventory("Dropper") :
                                                                    b.getType().equals(Material.FURNACE) ? new FurnaceInventory("Furnace") :
                                                                            b.getType().equals(Material.ENCHANTING_TABLE) ? new EnchantingInventory("Enchanting") : null;
            if (inv != null) {
                player.openInventory(inv);
                return;
            }
        }
        if (heldItem == null)
            return;
        if (!b.getType().isLiquid() && !b.getType().equals(Material.SNOW_LAYER) && !b.getType().isLongGrass())
            b = b.getRelative(Direction.fromFace(packet.getFace()));
        if (!b.getType().equals(Material.AIR))
            return;
        Location clicked = new Location(player.getWorld(), b.getLocation().getX() + packet.getCursorX(), b.getLocation().getY() +
                1 - (packet.getCursorY() == 0 ? 1 : packet.getCursorY()), b.getLocation().getZ() + packet.getCursorZ());
        Material setType = heldItem.getType();
        if (setType == null)
            return;
        if (setType.getParent().equals(Material.TORCH) || setType.getParent().equals(Material.REDSTONE_TORCH))//TODO: Check if is a valid torch position
            setType = packet.getFace().equals(Face.SOUTH) ? Material.fromString("EAST_" + setType.getParent()) :
                    packet.getFace().equals(Face.NORTH) ? Material.fromString("WEST_" + setType.getParent()) :
                            packet.getFace().equals(Face.WEST) ? Material.fromString("SOUTH_" + setType.getParent()) :
                                    packet.getFace().equals(Face.EAST) ? Material.fromString("NORTH_" + setType.getParent()) :
                                            Material.fromString("UP_" + setType.getParent());
        if (setType.equals(Material.REDSTONE))
            setType = Material.REDSTONE_WIRE;
        if (setType.equals(Material.STRING))
            setType = Material.TRIPWIRE;
        if (Material.fromString(setType.getName() + "_BLOCK") != null && !setType.equals(Material.BROWN_MUSHROOM) && !setType.equals(Material.RED_MUSHROOM) &&
                !setType.equals(Material.MELON))
            setType = Material.fromString(setType.getName() + "_BLOCK");
        String name = setType.getName().replace("_UP", "");
        if ((packet.getFace().equals(Face.BOTTOM) || packet.getFace().equals(Face.TOP)) && Material.fromString(name + "_UP") != null)
            setType = Material.fromString(name + "_UP");
        else if ((packet.getFace().equals(Face.NORTH) || packet.getFace().equals(Face.SOUTH)) && Material.fromString(name + "_EAST") != null)
            setType = Material.fromString(name + "_EAST");
        else if ((packet.getFace().equals(Face.EAST) || packet.getFace().equals(Face.WEST)) && Material.fromString(name + "_NORTH") != null)
            setType = Material.fromString(name + "_NORTH");
        if (setType.getName().contains("BUCKET"))
            setType = Material.fromString(setType.getName().replace("_BUCKET", ""));
        if (setType.getParent().equals(Material.SPAWN_EGG))
            clicked.getWorld().loadEntity(Entity.fromType(EntityType.fromString(setType.getName().replaceFirst("SPAWN_", "")), clicked));
        else if (setType.equals(Material.ARMOR_STAND))
            clicked.getWorld().loadEntity(Entity.fromType(EntityType.ARMOR_STAND, clicked));
        else
            b.setType(setType);
    }
}