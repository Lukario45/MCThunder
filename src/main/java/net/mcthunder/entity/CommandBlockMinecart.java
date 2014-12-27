package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;

public class CommandBlockMinecart extends Minecart {
    public CommandBlockMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_COMMAND_BLOCK;
        this.blockType = Material.COMMAND_BLOCK_MINECART;
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
    }
}