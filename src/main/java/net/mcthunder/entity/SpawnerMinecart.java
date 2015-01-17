package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.*;

public class SpawnerMinecart extends Minecart {
    private EntityType spawnType;

    public SpawnerMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_SPAWNER;
        this.blockType = Material.MOB_SPAWNER;
        this.spawnType = EntityType.PIG;
        this.metadata.setMetadata(20, 3407872);
    }

    public SpawnerMinecart(World w, CompoundTag tag) {
        super(w, tag);
        ListTag spawnPotentials = tag.get("SpawnPotentials");
        if(spawnPotentials != null)
            for (int j = 0; j < spawnPotentials.size(); j++) {
                CompoundTag spawnPotential = spawnPotentials.get(j);
                StringTag entityType = spawnPotential.get("Type");
                IntTag weight = spawnPotential.get("Weight");
                CompoundTag properties = spawnPotential.get("Properties");
            }
        StringTag entityID = tag.get("EntityId");
        CompoundTag spawnData = tag.get("SpawnData");
        ShortTag spawnCount = tag.get("SpawnCount");
        ShortTag spawnRange = tag.get("SpawnRange");
        ShortTag delay = tag.get("Delay");
        ShortTag minSpawnDelay = tag.get("MinSpawnDelay");
        ShortTag maxSpawnDelay = tag.get("MaxSpawnDelay");
        ShortTag maxNearbyEntities = tag.get("MaxNearbyEntities");
        ShortTag requiredPlayerRange = tag.get("RequiredPlayerRange");
        this.blockType = Material.MOB_SPAWNER;
        this.spawnType = EntityType.PIG;
        this.metadata.setMetadata(20, 3407872);
    }

    public void setSpawnType(EntityType spawnType) {
        this.spawnType = spawnType;
    }

    public EntityType getSpawnType() {
        return this.spawnType;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}