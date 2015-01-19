package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.HopperInventory;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.ListTag;

public class HopperMinecart extends Minecart {
    private HopperInventory inv;
    private int transferCooldown = 0;

    public HopperMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_HOPPER;
        this.inv = new HopperInventory(this.customName.equals("") ? "Hopper" : this.customName);
        this.blockType = Material.HOPPER;
        this.metadata.setMetadata(20, 10092544);
    }

    public HopperMinecart(World w, CompoundTag tag) {
        super(w, tag);
        this.inv = new HopperInventory(this.customName.equals("") ? "Hopper" : this.customName);
        IntTag transferCooldown = tag.get("TransferCooldown");
        if (transferCooldown != null)
            this.transferCooldown = transferCooldown.getValue();
        this.inv.setItems((ListTag) tag.get("Items"));
        this.blockType = Material.HOPPER;
        this.metadata.setMetadata(20, 10092544);
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

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TransferCooldown", this.transferCooldown));
        nbt.put(this.inv.getItemList("Items"));
        return nbt;
    }
}