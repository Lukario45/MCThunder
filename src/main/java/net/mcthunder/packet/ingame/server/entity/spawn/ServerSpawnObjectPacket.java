package net.mcthunder.packet.ingame.server.entity.spawn;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSpawnObjectPacket
        implements Packet {
    private int entityId;
    private Type type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private int data;
    private double motX;
    private double motY;
    private double motZ;

    private ServerSpawnObjectPacket() {
    }

    public ServerSpawnObjectPacket(int entityId, Type type, double x, double y, double z, float yaw, float pitch) {
        this(entityId, type, 0, x, y, z, yaw, pitch, 0.0D, 0.0D, 0.0D);
    }

    public ServerSpawnObjectPacket(int entityId, Type type, int data, double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ) {
        this.entityId = entityId;
        this.type = type;
        this.data = data;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }

    private static Type idToType(byte id) throws IOException {
        switch (id) {
            case 1:
                return Type.BOAT;
            case 2:
                return Type.ITEM;
            case 10:
                return Type.MINECART;
            case 50:
                return Type.PRIMED_TNT;
            case 51:
                return Type.ENDER_CRYSTAL;
            case 60:
                return Type.ARROW;
            case 61:
                return Type.SNOWBALL;
            case 62:
                return Type.EGG;
            case 63:
                return Type.GHAST_FIREBALL;
            case 64:
                return Type.BLAZE_FIREBALL;
            case 65:
                return Type.ENDER_PEARL;
            case 66:
                return Type.WITHER_HEAD_PROJECTILE;
            case 70:
                return Type.FALLING_BLOCK;
            case 71:
                return Type.ITEM_FRAME;
            case 72:
                return Type.EYE_OF_ENDER;
            case 73:
                return Type.POTION;
            case 75:
                return Type.EXP_BOTTLE;
            case 76:
                return Type.FIREWORK_ROCKET;
            case 77:
                return Type.LEASH_KNOT;
            case 90:
                return Type.FISH_HOOK;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 67:
            case 68:
            case 69:
            case 74:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
        }
        throw new IOException("Unknown object type id: " + id);
    }

    private static byte typeToId(Type type) throws IOException {
        switch (type.ordinal()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 10;
            case 4:
                return 50;
            case 5:
                return 51;
            case 6:
                return 60;
            case 7:
                return 61;
            case 8:
                return 62;
            case 9:
                return 63;
            case 10:
                return 64;
            case 11:
                return 65;
            case 12:
                return 66;
            case 13:
                return 70;
            case 14:
                return 71;
            case 15:
                return 72;
            case 16:
                return 73;
            case 17:
                return 75;
            case 18:
                return 76;
            case 19:
                return 77;
            case 20:
                return 90;
        }
        throw new IOException("Unmapped object type: " + type);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Type getType() {
        return this.type;
    }

    public int getData() {
        return this.data;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public double getMotionX() {
        return this.motX;
    }

    public double getMotionY() {
        return this.motY;
    }

    public double getMotionZ() {
        return this.motZ;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readVarInt();
        this.type = idToType(in.readByte());
        this.x = (in.readInt() / 32.0D);
        this.y = (in.readInt() / 32.0D);
        this.z = (in.readInt() / 32.0D);
        this.pitch = (in.readByte() * 360 / 256.0F);
        this.yaw = (in.readByte() * 360 / 256.0F);
        this.data = in.readInt();
        if (this.data > 0) {
            this.motX = (in.readShort() / 8000.0D);
            this.motY = (in.readShort() / 8000.0D);
            this.motZ = (in.readShort() / 8000.0D);
        }
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(typeToId(this.type));
        out.writeInt((int) (this.x * 32.0D));
        out.writeInt((int) (this.y * 32.0D));
        out.writeInt((int) (this.z * 32.0D));
        out.writeByte((byte) (int) (this.pitch * 256.0F / 360.0F));
        out.writeByte((byte) (int) (this.yaw * 256.0F / 360.0F));
        out.writeInt(this.data);
        if (this.data > 0) {
            out.writeShort((int) (this.motX * 8000.0D));
            out.writeShort((int) (this.motY * 8000.0D));
            out.writeShort((int) (this.motZ * 8000.0D));
        }
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Type {
        BOAT,
        ITEM,
        MINECART,
        PRIMED_TNT,
        ENDER_CRYSTAL,
        ARROW,
        SNOWBALL,
        EGG,
        GHAST_FIREBALL,
        BLAZE_FIREBALL,
        ENDER_PEARL,
        WITHER_HEAD_PROJECTILE,
        FALLING_BLOCK,
        ITEM_FRAME,
        EYE_OF_ENDER,
        POTION,
        EXP_BOTTLE,
        FIREWORK_ROCKET,
        LEASH_KNOT,
        FISH_HOOK;
    }

    public static class FallingBlockType {
        private int id;
        private int metadata;

        public FallingBlockType(int id, int metadata) {
            this.id = id;
            this.metadata = metadata;
        }

        public int getId() {
            return this.id;
        }

        public int getMetadata() {
            return this.metadata;
        }
    }

    public static class FallingBlockData {
        public static final int BLOCK_TYPE_TO_DATA(ServerSpawnObjectPacket.FallingBlockType type) {
            return BLOCK_TYPE_TO_DATA(type.getId(), type.getMetadata());
        }

        public static final int BLOCK_TYPE_TO_DATA(int block, int metadata) {
            return block | metadata << 16;
        }

        public static final ServerSpawnObjectPacket.FallingBlockType DATA_TO_BLOCK_TYPE(int data) {
            return new ServerSpawnObjectPacket.FallingBlockType(data & 0xFFFF, data >> 16);
        }
    }

    public static class ItemFrameDirection {
        public static final int SOUTH = 0;
        public static final int WEST = 1;
        public static final int NORTH = 2;
        public static final int EAST = 3;
    }

    public static class MinecartType {
        public static final int NORMAL = 0;
        public static final int CHEST = 1;
        public static final int POWERED = 2;
        public static final int TNT = 3;
        public static final int MOB_SPAWNER = 4;
        public static final int HOPPER = 5;
        public static final int COMMAND_BLOCK = 6;
    }
}