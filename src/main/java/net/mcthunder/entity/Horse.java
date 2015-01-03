package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.packetlib.packet.Packet;

public class Horse extends LivingEntity {
    private boolean isTame, hasSaddle, hasChest, isBred, isEating, isRearing, mouthOpen;
    private HorseType horseType;
    private String ownerName;
    private int color, style, armorType;

    public Horse(Location location) {
        super(location);
        this.type = EntityType.HORSE;
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.TAME, this.isTame = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.SADDLE, this.hasSaddle = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.CHEST, this.hasChest = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.BRED, this.isBred = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.EATING, this.isEating = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.REARING, this.isRearing = false);
        this.metadata.setBitOfInt(MetadataConstants.HORSE, MetadataConstants.HorseFlags.MOUTH_OPEN, this.mouthOpen = false);
        this.metadata.setMetadata(19, (this.horseType = HorseType.HORSE).getID());
        this.metadata.setMetadata(20, (this.color = HorseColor.WHITE) | (this.style = HorseStyle.NONE) << 8);
        this.metadata.setMetadata(21, this.ownerName = "");
        this.metadata.setMetadata(22, this.armorType = ArmorType.NONE);
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.HORSE, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

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