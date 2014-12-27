package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;

public class SpawnerMinecart extends Minecart {
    private EntityType spawnType;

    public SpawnerMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_SPAWNER;
        this.blockType = Material.MOB_SPAWNER;
        this.spawnType = EntityType.PIG;
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
    }

    public void setSpawnType(EntityType spawnType) {
        this.spawnType = spawnType;
    }

    public EntityType getSpawnType() {
        return this.spawnType;
    }
}