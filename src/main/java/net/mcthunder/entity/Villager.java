package net.mcthunder.entity;

import net.mcthunder.api.Location;

public class Villager extends Ageable {//TODO: Add a villager inventory with it calculating things
    private int villagerType;

    public Villager(Location location) {
        super(location);
        this.type = EntityType.VILLAGER;
        this.metadata.setMetadata(16, this.villagerType = VillagerType.FARMER);
    }

    @Override
    public void ai() {

    }

    public void setVillagerType(int villagerType) {
        this.metadata.setMetadata(16, this.villagerType = villagerType);
        updateMetadata();
    }

    public int getVillagerType() {
        return this.villagerType;
    }

    public class VillagerType {
        public static final int FARMER = 0;
        public static final int LIBRARIAN = 1;
        public static final int PRIEST = 2;
        public static final int BLACKSMITH = 3;
        public static final int BUTCHER = 4;
    }
}