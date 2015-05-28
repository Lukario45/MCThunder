package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.data.game.values.world.GenericSound;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

import java.util.Random;
import java.util.UUID;

public class Horse extends Ageable {
    private boolean isTame = false, hasSaddle = false, hasChest = false, isBred = false, isEating = false, isRearing = false, mouthOpen = false,
            hasReproduced = false;
    private int color = HorseColor.WHITE, style = HorseStyle.NONE, armorType = ArmorType.NONE, temper = 0;
    private HorseType horseType = HorseType.HORSE;
    private String ownerName = "";

    public Horse(Location location) {
        super(location);
        this.type = EntityType.HORSE;
        this.maxHealth = new Random().nextInt(16) + 15;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.TAME, this.isTame);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.SADDLE, this.hasSaddle);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.CHEST, this.hasChest);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.BRED, this.isBred);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.EATING, this.isEating);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.REARING, this.isRearing);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.MOUTH_OPEN, this.mouthOpen);
        this.metadata.setMetadata(19, this.horseType.getID());
        this.metadata.setMetadata(20, this.color | this.style << 8);
        this.metadata.setMetadata(21, this.ownerName);
        this.metadata.setMetadata(22, this.armorType);
    }

    public Horse(World w, CompoundTag tag) {
        super(w, tag);
        this.maxHealth = new Random().nextInt(16) + 15;
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        ByteTag bred = tag.get("Bred");
        ByteTag chestedHorse = tag.get("ChestedHorse");//1 true, 0 false
        ByteTag eatingHaystack = tag.get("EatingHaystack");//1 true, 0 false
        ByteTag hasReproduced = tag.get("HasReproduced");//1 true, 0 false
        ByteTag tame = tag.get("Tame");//1 true, 0 false
        IntTag temper = tag.get("Temper");
        IntTag horseType = tag.get("Type");
        IntTag variant = tag.get("Variant");
        StringTag tamerName = tag.get("OwnerName");
        StringTag ownerUUID = (StringTag) tag.get("OwnerUUID");
        UUID tamerUUID = UUID.randomUUID();
        try {
            if (ownerUUID != null)
                tamerUUID = UUID.fromString(ownerUUID.getValue());
        } catch (Exception e) { }
        //this.inv.setItems((ListTag) tag.get("Items"));
        CompoundTag armorItem = tag.get("ArmorItem");
        CompoundTag saddleItem = tag.get("SaddleItem");
        ByteTag saddle = tag.get("Saddle");//1 true, 0 false
        this.hasReproduced = hasReproduced != null && hasReproduced.getValue() == (byte) 1;
        if (temper != null)
            this.temper = temper.getValue();
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.TAME, this.isTame = tame != null && tame.getValue() == (byte) 1);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.SADDLE, this.hasSaddle = saddle != null && saddle.getValue() == (byte) 1);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.CHEST, this.hasChest = chestedHorse != null && chestedHorse.getValue() == (byte) 1);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.BRED, this.isBred = bred != null && bred.getValue() == (byte) 1);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.EATING, this.isEating = eatingHaystack != null && eatingHaystack.getValue() == (byte) 1);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.REARING, this.isRearing = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.MOUTH_OPEN, this.mouthOpen = false);
        if (horseType != null)
            this.horseType = HorseType.fromID((byte)(int) horseType.getValue());
        this.metadata.setMetadata(19, this.horseType.getID());
        this.metadata.setMetadata(20, this.color | this.style << 8);
        if (tamerName != null)
            this.ownerName = tamerName.getValue();
        this.metadata.setMetadata(21, this.ownerName);
        this.metadata.setMetadata(22, this.armorType);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.HORSE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    @Override
    public GenericSound getDeathSound() {
        return GenericSound.HORSE_DEATH;
    }

    @Override
    public GenericSound getHurtSound() {
        return GenericSound.HORSE_HURT;
    }

    public void setHasReproduced(boolean hasReproduced) {
        this.hasReproduced = hasReproduced;
    }

    public boolean hasReproduced() {
        return this.hasReproduced;
    }

    public void setTame(boolean isTame) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.TAME, this.isTame = isTame);
        updateMetadata();
    }

    public boolean isTame() {
        return this.isTame;
    }

    public void setHasSaddle(boolean hasSaddle) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.SADDLE, this.hasSaddle = hasSaddle);
        updateMetadata();
    }

    public boolean hasSaddle() {
        return this.hasSaddle;
    }

    public void setHasChest(boolean hasChest) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.CHEST, this.hasChest = hasChest);
        updateMetadata();
    }

    public boolean hasChest() {
        return this.hasChest;
    }

    public void setBred(boolean isBred) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.BRED, this.isBred = isBred);
        updateMetadata();
    }

    public boolean isBred() {
        return this.isBred;
    }

    public void setEating(boolean isEating) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.EATING, this.isEating = isEating);
        updateMetadata();
    }

    public boolean isEating() {
        return this.isEating;
    }

    public void setRearing(boolean isRearing) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.REARING, this.isRearing = isRearing);
        updateMetadata();
    }

    public boolean isRearing() {
        return this.isRearing;
    }

    public void setMouthOpen(boolean mouthOpen) {
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.MOUTH_OPEN, this.mouthOpen = mouthOpen);
        updateMetadata();
    }

    public boolean isMouthOpen() {
        return this.mouthOpen;
    }

    public void setHorseType(HorseType horseType) {
        this.metadata.setMetadata(19, (this.horseType = horseType).getID());
        updateMetadata();
    }

    public HorseType getHorseType() {
        return this.horseType;
    }

    public void setColor(int color) {
        this.metadata.setMetadata(20, (this.color = color) | this.style << 8);
        updateMetadata();
    }

    public int getColor() {
        return this.color;
    }

    public void setTemper(int temper) {
        this.temper = temper;
    }

    public int getTemper() {
        return this.temper;
    }

    public void setStyle(int style) {
        this.metadata.setMetadata(20, this.color | (this.style = style) << 8);
        updateMetadata();
    }

    public int getStyle() {
        return this.style;
    }

    public int getVariant() {
        return this.color | this.style << 8;
    }

    public void setOwnerName(String ownerName) {
        this.metadata.setMetadata(21, this.ownerName = ownerName);
        updateMetadata();
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setArmorType(int armorType) {
        this.metadata.setMetadata(22, this.armorType = armorType);
        updateMetadata();
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }

    public int getArmorType() {
        return this.armorType;
    }

    public enum HorseType {
        HORSE((byte) 0),
        DONKEY((byte) 1),
        MULE((byte) 2),
        ZOMBIE((byte) 3),
        SKELETON((byte) 4);

        private byte horseID;

        private HorseType(byte horseID) {
            this.horseID = horseID;
        }

        public byte getID() {
            return this.horseID;
        }

        public static HorseType fromID(byte id) {
            for (HorseType h : values())
                if (h.getID() == id)
                    return h;
            return HORSE;
        }
    }

    public class HorseColor {
        public static final int WHITE = 0;
        public static final int CREAMY = 1;
        public static final int CHESTNUT = 2;
        public static final int BROWN = 3;
        public static final int BLACK = 4;
        public static final int GRAY = 5;
        public static final int DARK_BROWN = 6;
    }

    public class HorseStyle {
        public static final int NONE = 0;
        public static final int WHITE = 1;
        public static final int WHITEFIELD = 2;
        public static final int WHITE_DOTS = 3;
        public static final int BLACK_DOTS = 4;
    }

    public class ArmorType {
        public static final int NONE = 0;
        public static final int IRON = 1;
        public static final int GOLD = 2;
        public static final int DIAMOND = 3;
    }
}