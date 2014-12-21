package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class CommandBlockMinecart extends Minecart {
    public CommandBlockMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_COMMAND_BLOCK;
        this.block = 137;
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
    }
}