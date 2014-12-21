package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class SpawnerMinecart extends Minecart {
    private EntityType spawnType;

    public SpawnerMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_SPAWNER;
        this.block = 52;
        this.spawnType = EntityType.PIG;
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
    }
}