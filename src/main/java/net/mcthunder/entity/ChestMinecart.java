package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ChestInventory;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ListTag;

public class ChestMinecart extends Minecart {
    private ChestInventory inv;

    public ChestMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_CHEST;
        this.inv = new ChestInventory(this.customName.equals("") ? "Chest" : this.customName);
        this.blockType = Material.CHEST;
        this.metadata.setMetadata(20, 3538944);
    }

    public ChestMinecart(World w, CompoundTag tag) {
        super(w, tag);
        this.inv = new ChestInventory(this.customName.equals("") ? "Chest" : this.customName);
        this.inv.setItems((ListTag) tag.get("Items"));
        this.blockType = Material.CHEST;
        this.metadata.setMetadata(20, 3538944);
    }

    public ChestInventory getInventory() {
        return this.inv;
    }
}