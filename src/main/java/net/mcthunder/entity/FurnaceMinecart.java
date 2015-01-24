package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MinecartType;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class FurnaceMinecart extends Minecart {
    private int transferCooldown = 0;
    private boolean isPowered = false;

    public FurnaceMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_FURNACE;
        this.blockType = Material.FURNACE_MINECART;
        this.metadata.setMetadata(16, (byte) (this.isPowered ? 1 : 0));
        this.metadata.setMetadata(20, 61);
    }

    public FurnaceMinecart(World w, CompoundTag tag) {
        super(w, tag);
        IntTag transferCooldown = tag.get("TransferCooldown");
        if (transferCooldown != null)
            this.transferCooldown = transferCooldown.getValue();
        this.blockType = Material.FURNACE_MINECART;
        this.metadata.setMetadata(16, (byte) (this.isPowered ? 1 : 0));
        this.metadata.setMetadata(20, 61);
    }

    public void setPowered(boolean isPowered) {
        this.metadata.setMetadata(16, (byte) ((this.isPowered = isPowered) ? 1 : 0));
        updateMetadata();
    }

    public boolean isPowered() {
        return this.isPowered;
    }

    public void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    public int getTransferCooldown() {
        return this.transferCooldown;
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.MINECART, MinecartType.POWERED, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TransferCooldown", this.transferCooldown));
        return nbt;
    }
}