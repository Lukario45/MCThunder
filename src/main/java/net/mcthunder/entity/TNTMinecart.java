package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;

public class TNTMinecart extends Minecart {
    private int fuse = 0;

    public TNTMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_TNT;
        this.blockType = Material.TNT;
        this.metadata.setMetadata(20, 3014656);
    }

    public TNTMinecart(World w, CompoundTag tag) {
        super(w, tag);
        IntTag fuse = tag.get("TNTFuse");
        if (fuse != null)
            this.fuse = fuse.getValue();
        this.blockType = Material.TNT;
        this.metadata.setMetadata(20, 3014656);
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuse) {
        this.fuse = fuse;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TNTFuse", this.fuse));
        return nbt;
    }
}