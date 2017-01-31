package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.block.Material;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.object.MinecartType;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.packetlib.packet.Packet;

public class ChestMinecart extends Minecart {
    private ChestInventory inv;

    public ChestMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_CHEST;
        this.inv = new ChestInventory(this.customName.equals("") ? "Chest" : this.customName);
        this.blockType = Material.CHEST;
        this.metadata.setMetadata(20, 54);
    }

    public ChestMinecart(World w, CompoundTag tag) {
        super(w, tag);
        this.inv = new ChestInventory(this.customName.equals("") ? "Chest" : this.customName);
        this.inv.setItems((ListTag) tag.get("Items"));
        this.blockType = Material.CHEST;
        this.metadata.setMetadata(20, 54);
    }

    public ChestInventory getInventory() {
        return this.inv;
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, null,ObjectType.MINECART, MinecartType.CHEST, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(this.inv.getItemList("Items"));
        return nbt;
    }
}