package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerBlockValuePacket
        implements Packet {
    private static final int NOTE_BLOCK = 25;
    private static final int STICKY_PISTON = 29;
    private static final int PISTON = 33;
    private static final int MOB_SPAWNER = 52;
    private static final int CHEST = 54;
    private static final int ENDER_CHEST = 130;
    private static final int TRAPPED_CHEST = 146;
    private int x;
    private int y;
    private int z;
    private ValueType type;
    private Value value;
    private int blockId;

    private ServerBlockValuePacket() {
    }

    public ServerBlockValuePacket(int x, int y, int z, ValueType type, Value value, int blockId) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.value = value;
        this.blockId = blockId;
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

    public ValueType getType() {
        return this.type;
    }

    public Value getValue() {
        return this.value;
    }

    public int getBlockId() {
        return this.blockId;
    }

    public void read(NetIn in) throws IOException {
        this.x = in.readInt();
        this.y = in.readShort();
        this.z = in.readInt();
        this.type = intToType(in.readUnsignedByte());
        this.value = intToValue(in.readUnsignedByte());
        this.blockId = (in.readVarInt() & 0xFFF);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.x);
        out.writeShort(this.y);
        out.writeInt(this.z);
        out.writeByte(typeToInt(this.type));
        out.writeByte(valueToInt(this.value));
        out.writeVarInt(this.blockId & 0xFFF);
    }

    public boolean isPriority() {
        return false;
    }

    private ValueType intToType(int value) throws IOException {
        if (this.blockId == 25) {
            if (value == 0)
                return NoteBlockValueType.HARP;
            if (value == 1)
                return NoteBlockValueType.DOUBLE_BASS;
            if (value == 2)
                return NoteBlockValueType.SNARE_DRUM;
            if (value == 3)
                return NoteBlockValueType.HI_HAT;
            if (value == 4)
                return NoteBlockValueType.BASS_DRUM;
        } else if ((this.blockId == 29) || (this.blockId == 33)) {
            if (value == 0)
                return PistonValueType.PUSHING;
            if (value == 1)
                return PistonValueType.PULLING;
        } else if (this.blockId == 52) {
            if (value == 1)
                return MobSpawnerValueType.RESET_DELAY;
        } else if ((this.blockId == 54) || (this.blockId == 130) || (this.blockId == 146)) {
            if (value == 1)
                return ChestValueType.VIEWING_PLAYER_COUNT;
        } else {
            return GenericValueType.GENERIC;
        }

        throw new IOException("Unknown value type id: " + value + ", " + this.blockId);
    }

    private int typeToInt(ValueType type) throws IOException {
        if (type == NoteBlockValueType.HARP)
            return 0;
        if (type == NoteBlockValueType.DOUBLE_BASS)
            return 1;
        if (type == NoteBlockValueType.SNARE_DRUM)
            return 2;
        if (type == NoteBlockValueType.HI_HAT)
            return 3;
        if (type == NoteBlockValueType.BASS_DRUM) {
            return 4;
        }

        if (type == PistonValueType.PUSHING)
            return 0;
        if (type == PistonValueType.PULLING) {
            return 1;
        }

        if (type == MobSpawnerValueType.RESET_DELAY) {
            return 1;
        }

        if (type == ChestValueType.VIEWING_PLAYER_COUNT) {
            return 1;
        }

        if (type == GenericValueType.GENERIC) {
            return 0;
        }

        throw new IOException("Unmapped value type: " + type);
    }

    private Value intToValue(int value) throws IOException {
        if (this.blockId == 25)
            return new NoteBlockValue(value);
        if ((this.blockId == 29) || (this.blockId == 33)) {
            if (value == 0)
                return PistonValue.DOWN;
            if (value == 1)
                return PistonValue.UP;
            if (value == 2)
                return PistonValue.SOUTH;
            if (value == 3)
                return PistonValue.WEST;
            if (value == 4)
                return PistonValue.NORTH;
            if (value == 5)
                return PistonValue.EAST;
        } else if (this.blockId == 52) {
            if (value == 0)
                return MobSpawnerValue.VALUE;
        } else {
            if ((this.blockId == 54) || (this.blockId == 130) || (this.blockId == 146)) {
                return new ChestValue(value);
            }
            return new GenericValue(value);
        }

        throw new IOException("Unknown value id: " + value + ", " + this.blockId);
    }

    private int valueToInt(Value value) throws IOException {
        if ((value instanceof NoteBlockValue)) {
            return ((NoteBlockValue) value).getPitch();
        }

        if (value == PistonValue.DOWN)
            return 0;
        if (value == PistonValue.UP)
            return 1;
        if (value == PistonValue.SOUTH)
            return 2;
        if (value == PistonValue.WEST)
            return 3;
        if (value == PistonValue.NORTH)
            return 4;
        if (value == PistonValue.EAST) {
            return 5;
        }

        if (value == MobSpawnerValue.VALUE) {
            return 0;
        }

        if ((value instanceof ChestValue)) {
            return ((ChestValue) value).getViewers();
        }

        if ((value instanceof GenericValue)) {
            return ((GenericValue) value).getValue();
        }

        throw new IOException("Unmapped value: " + value);
    }

    public static enum MobSpawnerValue
            implements ServerBlockValuePacket.Value {
        VALUE;
    }

    public static enum PistonValue
            implements ServerBlockValuePacket.Value {
        DOWN,
        UP,
        SOUTH,
        WEST,
        NORTH,
        EAST;
    }

    public static enum MobSpawnerValueType
            implements ServerBlockValuePacket.ValueType {
        RESET_DELAY;
    }

    public static enum ChestValueType
            implements ServerBlockValuePacket.ValueType {
        VIEWING_PLAYER_COUNT;
    }

    public static enum PistonValueType
            implements ServerBlockValuePacket.ValueType {
        PUSHING,
        PULLING;
    }

    public static enum NoteBlockValueType
            implements ServerBlockValuePacket.ValueType {
        HARP,
        DOUBLE_BASS,
        SNARE_DRUM,
        HI_HAT,
        BASS_DRUM;
    }

    public static enum GenericValueType
            implements ServerBlockValuePacket.ValueType {
        GENERIC;
    }

    public static abstract interface Value {
    }

    public static abstract interface ValueType {
    }

    public static class ChestValue
            implements ServerBlockValuePacket.Value {
        private int viewers;

        public ChestValue(int viewers) {
            this.viewers = viewers;
        }

        public int getViewers() {
            return this.viewers;
        }
    }

    public static class NoteBlockValue
            implements ServerBlockValuePacket.Value {
        private int pitch;

        public NoteBlockValue(int pitch) {
            if ((pitch < 0) || (pitch > 24)) {
                throw new IllegalArgumentException("Pitch must be between 0 and 24.");
            }

            this.pitch = pitch;
        }

        public int getPitch() {
            return this.pitch;
        }
    }

    public static class GenericValue
            implements ServerBlockValuePacket.Value {
        private int value;

        public GenericValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}