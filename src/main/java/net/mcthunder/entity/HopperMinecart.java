package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.block.Material;
import net.mcthunder.inventory.HopperInventory;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.object.MinecartType;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.packetlib.packet.Packet;

public class HopperMinecart extends Minecart {
    private HopperInventory inv;
    private int transferCooldown = 0;

    public HopperMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_HOPPER;
        this.inv = new HopperInventory(this.customName.equals("") ? "Hopper" : this.customName);
        this.blockType = Material.HOPPER;
        this.metadata.setMetadata(20, 154);
    }

    public HopperMinecart(World w, CompoundTag tag) {
        super(w, tag);
        this.inv = new HopperInventory(this.customName.equals("") ? "Hopper" : this.customName);
        IntTag transferCooldown = tag.get("TransferCooldown");
        if (transferCooldown != null)
            this.transferCooldown = transferCooldown.getValue();
        this.inv.setItems((ListTag) tag.get("Items"));
        this.blockType = Material.HOPPER;
        this.metadata.setMetadata(20, 154);
    }

    public HopperInventory getInventory() {
        return this.inv;
    }

    public void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    public int getTransferCooldown() {
        return this.transferCooldown;
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, null, ObjectType.MINECART, MinecartType.HOPPER, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TransferCooldown", this.transferCooldown));
        nbt.put(this.inv.getItemList("Items"));
        return nbt;
    }
}