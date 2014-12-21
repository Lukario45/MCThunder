package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Villager extends Ageable {
    private int villagerType;

    public Villager(Location location) {
        super(location);
        this.type = EntityType.VILLAGER;
        this.villagerType = VillagerType.FARMER;
        this.metadata.setMetadata(16, this.villagerType);
    }

    @Override
    public void ai() {

    }

    public class VillagerType {
        public static final int FARMER = 0;
        public static final int LIBRARIAN = 1;
        public static final int PRIEST = 2;
        public static final int BLACKSMITH = 3;
        public static final int BUTCHER = 4;
    }
}