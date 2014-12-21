package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;

public class Horse extends LivingEntity {
    private boolean isTame, hasSaddle, hasChest, isBred, isEating, isRearing, mouthOpen;
    private HorseType horseType;
    private String ownerName;
    private int color, style, armorType;

    public Horse(Location location) {
        super(location);
        this.type = EntityType.HORSE;
        this.isTame = false;
        this.hasSaddle = false;
        this.hasChest = false;
        this.isBred = false;
        this.isEating = false;
        this.isRearing = false;
        this.mouthOpen = false;
        this.horseType = HorseType.HORSE;
        this.ownerName = "";
        this.color = HorseColor.WHITE;
        this.style = HorseStyle.NONE;
        this.armorType = ArmorType.NONE;
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.TAME, this.isTame);
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.SADDLE, this.hasSaddle);
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.CHEST, this.hasChest);
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.BRED, this.isBred);
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.EATING, this.isEating);
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.REARING, this.isRearing);
        this.metadata.setBit(MetadataConstants.HORSE, MetadataConstants.HorseFlags.MOUTH_OPEN, this.mouthOpen);
        this.metadata.setMetadata(19, this.horseType.getID());
        this.metadata.setMetadata(20, this.color | this.style << 8);
        this.metadata.setMetadata(21, this.ownerName);
        this.metadata.setMetadata(22, this.armorType);
    }

    @Override
    public void ai() {

    }

    public enum HorseType {
        HORSE(0),
        DONKEY(1),
        MULE(2),
        ZOMBIE(3),
        SKELETON(4);

        private int horseID;

        private HorseType(int horseID) {
            this.horseID = horseID;
        }

        public int getID() {
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