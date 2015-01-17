package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;

public class TNTMinecart extends Minecart {
    private int fuse;

    public TNTMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_TNT;
        this.blockType = Material.TNT;
        this.fuse = 0;
        this.metadata.setMetadata(20, 3014656);
    }

    public TNTMinecart(World w, CompoundTag tag) {
        super(w, tag);
        IntTag fuse = tag.get("TNTFuse");
        this.fuse = fuse == null ? 0 : fuse.getValue();
        this.blockType = Material.TNT;
        this.metadata.setMetadata(20, 3014656);
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}