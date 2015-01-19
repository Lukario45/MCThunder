package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;

public class FurnaceMinecart extends Minecart {
    private int transferCooldown = 0;
    private boolean isPowered = false;

    public FurnaceMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_FURNACE;
        this.blockType = Material.FURNACE_MINECART;
        this.metadata.setMetadata(16, (byte) (this.isPowered ? 1 : 0));
        this.metadata.setMetadata(20, 3997696);
    }

    public FurnaceMinecart(World w, CompoundTag tag) {
        super(w, tag);
        IntTag transferCooldown = tag.get("TransferCooldown");
        if (transferCooldown != null)
            this.transferCooldown = transferCooldown.getValue();
        this.blockType = Material.FURNACE_MINECART;
        this.metadata.setMetadata(16, (byte) (this.isPowered ? 1 : 0));
        this.metadata.setMetadata(20, 3997696);
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

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TransferCooldown", this.transferCooldown));
        return nbt;
    }
}