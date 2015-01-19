package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.packetlib.packet.Packet;

public class Villager extends Ageable {//TODO: Add a villager inventory with it calculating things
    private int villagerType = VillagerType.FARMER;

    public Villager(Location location) {
        super(location);
        this.type = EntityType.VILLAGER;
        this.metadata.setMetadata(16, this.villagerType);
    }

    public Villager(World w, CompoundTag tag) {
        super(w, tag);
        IntTag profession = tag.get("Profession");
        IntTag riches = tag.get("Riches");
        IntTag career = tag.get("Career");
        IntTag careerLevel = tag.get("CareerLevel");
        ByteTag willing = tag.get("Willing");//1 true, 0 false
        //this.inv.setItems((ListTag) tag.get("Inventory"));
        CompoundTag offers = tag.get("Offers");
        ListTag recipes = offers.get("Recipes");
        if (recipes != null)
            for (int j = 0; j < recipes.size(); j++) {
                CompoundTag recipe = recipes.get(j);
                ByteTag rewardExp = recipe.get("rewardExp");//1 true, 0 false
                IntTag maxUses = recipe.get("maxUses");
                IntTag uses = recipe.get("uses");
                CompoundTag buy = recipe.get("buy");
                CompoundTag buyB = recipe.get("buyB");
                CompoundTag sell = recipe.get("sell");
            }
        this.metadata.setMetadata(16, this.villagerType);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.VILLAGER, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
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

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }

    public class VillagerType {
        public static final int FARMER = 0;
        public static final int LIBRARIAN = 1;
        public static final int PRIEST = 2;
        public static final int BLACKSMITH = 3;
        public static final int BUTCHER = 4;
    }
}