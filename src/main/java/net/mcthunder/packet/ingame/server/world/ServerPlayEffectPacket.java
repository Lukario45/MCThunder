package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerPlayEffectPacket
        implements Packet {
    private Effect effect;
    private int x;
    private int y;
    private int z;
    private EffectData data;
    private boolean broadcast;

    private ServerPlayEffectPacket() {
    }

    public ServerPlayEffectPacket(Effect effect, int x, int y, int z, EffectData data) {
        this(effect, x, y, z, data, false);
    }

    public ServerPlayEffectPacket(Effect effect, int x, int y, int z, EffectData data, boolean broadcast) {
        this.effect = effect;
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
        this.broadcast = broadcast;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public EffectData getData() {
        return this.data;
    }

    public boolean getBroadcast() {
        return this.broadcast;
    }

    public void read(NetIn in) throws IOException {
        this.effect = idToEffect(in.readInt());
        this.x = in.readInt();
        this.y = in.readUnsignedByte();
        this.z = in.readInt();
        this.data = valueToData(in.readInt());
        this.broadcast = in.readBoolean();
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(effectToId(this.effect));
        out.writeInt(this.x);
        out.writeByte(this.y);
        out.writeInt(this.z);
        out.writeInt(dataToValue(this.data));
        out.writeBoolean(this.broadcast);
    }

    public boolean isPriority() {
        return false;
    }

    private Effect idToEffect(int id) throws IOException {
        switch (id) {
            case 1000:
                return SoundEffect.CLICK;
            case 1001:
                return SoundEffect.EMPTY_DISPENSER_CLICK;
            case 1002:
                return SoundEffect.FIRE_PROJECTILE;
            case 1003:
                return SoundEffect.DOOR;
            case 1004:
                return SoundEffect.FIZZLE;
            case 1005:
                return SoundEffect.PLAY_RECORD;
            case 1007:
                return SoundEffect.GHAST_CHARGE;
            case 1008:
                return SoundEffect.GHAST_FIRE;
            case 1009:
                return SoundEffect.BLAZE_FIRE;
            case 1010:
                return SoundEffect.POUND_WOODEN_DOOR;
            case 1011:
                return SoundEffect.POUND_METAL_DOOR;
            case 1012:
                return SoundEffect.BREAK_WOODEN_DOOR;
            case 1014:
                return SoundEffect.WITHER_SHOOT;
            case 1015:
                return SoundEffect.BAT_TAKE_OFF;
            case 1016:
                return SoundEffect.INFECT_VILLAGER;
            case 1017:
                return SoundEffect.DISINFECT_VILLAGER;
            case 1018:
                return SoundEffect.ENDER_DRAGON_DEATH;
            case 1020:
                return SoundEffect.ANVIL_BREAK;
            case 1021:
                return SoundEffect.ANVIL_USE;
            case 1022:
                return SoundEffect.ANVIL_LAND;
            case 1006:
            case 1013:
            case 1019:
        }
        switch (id) {
            case 2000:
                return ParticleEffect.SMOKE;
            case 2001:
                return ParticleEffect.BREAK_BLOCK;
            case 2002:
                return ParticleEffect.BREAK_SPLASH_POTION;
            case 2003:
                return ParticleEffect.BREAK_EYE_OF_ENDER;
            case 2004:
                return ParticleEffect.MOB_SPAWN;
            case 2005:
                return ParticleEffect.BONEMEAL_GROW;
            case 2006:
                return ParticleEffect.HARD_LANDING_DUST;
        }

        throw new IOException("Unknown effect id: " + id);
    }

    private int effectToId(Effect effect) throws IOException {
        if (effect == SoundEffect.CLICK)
            return 1000;
        if (effect == SoundEffect.EMPTY_DISPENSER_CLICK)
            return 1001;
        if (effect == SoundEffect.FIRE_PROJECTILE)
            return 1002;
        if (effect == SoundEffect.DOOR)
            return 1003;
        if (effect == SoundEffect.FIZZLE)
            return 1004;
        if (effect == SoundEffect.PLAY_RECORD)
            return 1005;
        if (effect == SoundEffect.GHAST_CHARGE)
            return 1007;
        if (effect == SoundEffect.GHAST_FIRE)
            return 1008;
        if (effect == SoundEffect.BLAZE_FIRE)
            return 1009;
        if (effect == SoundEffect.POUND_WOODEN_DOOR)
            return 1010;
        if (effect == SoundEffect.POUND_METAL_DOOR)
            return 1011;
        if (effect == SoundEffect.BREAK_WOODEN_DOOR)
            return 1012;
        if (effect == SoundEffect.WITHER_SHOOT)
            return 1014;
        if (effect == SoundEffect.BAT_TAKE_OFF)
            return 1015;
        if (effect == SoundEffect.INFECT_VILLAGER)
            return 1016;
        if (effect == SoundEffect.DISINFECT_VILLAGER)
            return 1017;
        if (effect == SoundEffect.ENDER_DRAGON_DEATH)
            return 1018;
        if (effect == SoundEffect.ANVIL_BREAK)
            return 1020;
        if (effect == SoundEffect.ANVIL_USE)
            return 1021;
        if (effect == SoundEffect.ANVIL_LAND) {
            return 1022;
        }

        if (effect == ParticleEffect.SMOKE)
            return 2000;
        if (effect == ParticleEffect.BREAK_BLOCK)
            return 2001;
        if (effect == ParticleEffect.BREAK_SPLASH_POTION)
            return 2002;
        if (effect == ParticleEffect.BREAK_EYE_OF_ENDER)
            return 2003;
        if (effect == ParticleEffect.MOB_SPAWN)
            return 2004;
        if (effect == ParticleEffect.BONEMEAL_GROW)
            return 2005;
        if (effect == ParticleEffect.HARD_LANDING_DUST) {
            return 2006;
        }

        throw new IOException("Unmapped effect: " + effect);
    }

    private EffectData valueToData(int value) {
        if (this.effect == SoundEffect.PLAY_RECORD)
            return new RecordData(value);
        if (this.effect == ParticleEffect.SMOKE) {
            if (value == 0)
                return SmokeData.SOUTH_EAST;
            if (value == 1)
                return SmokeData.SOUTH;
            if (value == 2)
                return SmokeData.SOUTH_WEST;
            if (value == 3)
                return SmokeData.EAST;
            if (value == 4)
                return SmokeData.UP;
            if (value == 5)
                return SmokeData.WEST;
            if (value == 6)
                return SmokeData.NORTH_EAST;
            if (value == 7)
                return SmokeData.NORTH;
            if (value == 8)
                return SmokeData.NORTH_WEST;
        } else {
            if (this.effect == ParticleEffect.BREAK_BLOCK)
                return new BreakBlockData(value);
            if (this.effect == ParticleEffect.BREAK_SPLASH_POTION)
                return new BreakPotionData(value);
            if (this.effect == ParticleEffect.HARD_LANDING_DUST) {
                return new HardLandingData(value);
            }
        }
        return null;
    }

    private int dataToValue(EffectData data) {
        if ((data instanceof RecordData)) {
            return ((RecordData) data).getRecordId();
        }

        if ((data instanceof SmokeData)) {
            if (data == SmokeData.SOUTH_EAST)
                return 0;
            if (data == SmokeData.SOUTH)
                return 1;
            if (data == SmokeData.SOUTH_WEST)
                return 2;
            if (data == SmokeData.EAST)
                return 3;
            if (data == SmokeData.UP)
                return 4;
            if (data == SmokeData.WEST)
                return 5;
            if (data == SmokeData.NORTH_EAST)
                return 6;
            if (data == SmokeData.NORTH)
                return 7;
            if (data == SmokeData.NORTH_WEST) {
                return 8;
            }
        }

        if ((data instanceof BreakBlockData)) {
            return ((BreakBlockData) data).getBlockId();
        }

        if ((data instanceof BreakPotionData)) {
            return ((BreakPotionData) data).getPotionId();
        }

        if ((data instanceof HardLandingData)) {
            return ((HardLandingData) data).getDamagingDistance();
        }

        return 0;
    }

    public static class HardLandingData
            implements ServerPlayEffectPacket.EffectData {
        private int damagingDistance;

        public HardLandingData(int damagingDistance) {
            this.damagingDistance = damagingDistance;
        }

        public int getDamagingDistance() {
            return this.damagingDistance;
        }
    }

    public static class BreakPotionData
            implements ServerPlayEffectPacket.EffectData {
        private int potionId;

        public BreakPotionData(int potionId) {
            this.potionId = potionId;
        }

        public int getPotionId() {
            return this.potionId;
        }
    }

    public static class BreakBlockData
            implements ServerPlayEffectPacket.EffectData {
        private int blockId;

        public BreakBlockData(int blockId) {
            this.blockId = blockId;
        }

        public int getBlockId() {
            return this.blockId;
        }
    }

    public static enum SmokeData
            implements ServerPlayEffectPacket.EffectData {
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        EAST,
        UP,
        WEST,
        NORTH_EAST,
        NORTH,
        NORTH_WEST;
    }

    public static class RecordData
            implements ServerPlayEffectPacket.EffectData {
        private int recordId;

        public RecordData(int recordId) {
            this.recordId = recordId;
        }

        public int getRecordId() {
            return this.recordId;
        }
    }

    public static abstract interface EffectData {
    }

    public static enum ParticleEffect
            implements ServerPlayEffectPacket.Effect {
        SMOKE,
        BREAK_BLOCK,
        BREAK_SPLASH_POTION,
        BREAK_EYE_OF_ENDER,
        MOB_SPAWN,
        BONEMEAL_GROW,
        HARD_LANDING_DUST;
    }

    public static enum SoundEffect
            implements ServerPlayEffectPacket.Effect {
        CLICK,
        EMPTY_DISPENSER_CLICK,
        FIRE_PROJECTILE,
        DOOR,
        FIZZLE,
        PLAY_RECORD,
        GHAST_CHARGE,
        GHAST_FIRE,
        BLAZE_FIRE,
        POUND_WOODEN_DOOR,
        POUND_METAL_DOOR,
        BREAK_WOODEN_DOOR,
        WITHER_SHOOT,
        BAT_TAKE_OFF,
        INFECT_VILLAGER,
        DISINFECT_VILLAGER,
        ENDER_DRAGON_DEATH,
        ANVIL_BREAK,
        ANVIL_USE,
        ANVIL_LAND;
    }

    public static class RecordData
            implements ServerPlayEffectPacket.EffectData {
        private int recordId;

        public RecordData(int recordId) {
            this.recordId = recordId;
        }

        public int getRecordId() {
            return this.recordId;
        }
    }

    public static abstract interface Effect {
    }
}