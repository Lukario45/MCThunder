package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Villager extends Ageable {//TODO: Add a villager inventory with it calculating things
    private int villagerType;

    public Villager(Location location) {
        super(location);
        this.type = EntityType.VILLAGER;
        this.metadata.setMetadata(16, this.villagerType = VillagerType.FARMER);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.VILLAGER, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
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