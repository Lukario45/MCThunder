package net.mcthunder.packet.ingame.server.entity.spawn;

import net.mcthunder.game.essentials.EntityMetadata;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSpawnMobPacket
        implements Packet {
    private int entityId;
    private Type type;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private float headYaw;
    private double motX;
    private double motY;
    private double motZ;
    private EntityMetadata[] metadata;

    private ServerSpawnMobPacket() {
    }

    public ServerSpawnMobPacket(int entityId, Type type, double x, double y, double z, float yaw, float pitch, float headYaw, double motX, double motY, double motZ, EntityMetadata[] metadata) {
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headYaw = headYaw;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
        this.metadata = metadata;
    }

    private static Type idToType(byte id) throws IOException {
        switch (id) {
            case 50:
                return Type.CREEPER;
            case 51:
                return Type.SKELETON;
            case 52:
                return Type.SPIDER;
            case 53:
                return Type.GIANT_ZOMBIE;
            case 54:
                return Type.ZOMBIE;
            case 55:
                return Type.SLIME;
            case 56:
                return Type.GHAST;
            case 57:
                return Type.ZOMBIE_PIGMAN;
            case 58:
                return Type.ENDERMAN;
            case 59:
                return Type.CAVE_SPIDER;
            case 60:
                return Type.SILVERFISH;
            case 61:
                return Type.BLAZE;
            case 62:
                return Type.MAGMA_CUBE;
            case 63:
                return Type.ENDER_DRAGON;
            case 64:
                return Type.WITHER;
            case 65:
                return Type.BAT;
            case 66:
                return Type.WITCH;
            case 90:
                return Type.PIG;
            case 91:
                return Type.SHEEP;
            case 92:
                return Type.COW;
            case 93:
                return Type.CHICKEN;
            case 94:
                return Type.SQUID;
            case 95:
                return Type.WOLF;
            case 96:
                return Type.MOOSHROOM;
            case 97:
                return Type.SNOWMAN;
            case 98:
                return Type.OCELOT;
            case 99:
                return Type.IRON_GOLEM;
            case 100:
                return Type.HORSE;
            case 120:
                return Type.VILLAGER;
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
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
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
        }
        throw new IOException("Unknown mob type id: " + id);
    }

    private static byte typeToId(Type type) throws IOException {
        switch (type.ordinal()) {
            case 1:
                return 50;
            case 2:
                return 51;
            case 3:
                return 52;
            case 4:
                return 53;
            case 5:
                return 54;
            case 6:
                return 55;
            case 7:
                return 56;
            case 8:
                return 57;
            case 9:
                return 58;
            case 10:
                return 59;
            case 11:
                return 60;
            case 12:
                return 61;
            case 13:
                return 62;
            case 14:
                return 63;
            case 15:
                return 64;
            case 16:
                return 65;
            case 17:
                return 66;
            case 18:
                return 90;
            case 19:
                return 91;
            case 20:
                return 92;
            case 21:
                return 93;
            case 22:
                return 94;
            case 23:
                return 95;
            case 24:
                return 96;
            case 25:
                return 97;
            case 26:
                return 98;
            case 27:
                return 99;
            case 28:
                return 100;
            case 29:
                return 120;
        }
        throw new IOException("Unmapped mob type: " + type);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Type getType() {
        return this.type;
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

    public float getHeadYaw() {
        return this.headYaw;
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

    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readVarInt();
        this.type = idToType(in.readByte());
        this.x = (in.readInt() / 32.0D);
        this.y = (in.readInt() / 32.0D);
        this.z = (in.readInt() / 32.0D);
        this.yaw = (in.readByte() * 360 / 256.0F);
        this.pitch = (in.readByte() * 360 / 256.0F);
        this.headYaw = (in.readByte() * 360 / 256.0F);
        this.motX = (in.readShort() / 8000.0D);
        this.motY = (in.readShort() / 8000.0D);
        this.motZ = (in.readShort() / 8000.0D);
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(typeToId(this.type));
        out.writeInt((int) (this.x * 32.0D));
        out.writeInt((int) (this.y * 32.0D));
        out.writeInt((int) (this.z * 32.0D));
        out.writeByte((byte) (int) (this.yaw * 256.0F / 360.0F));
        out.writeByte((byte) (int) (this.pitch * 256.0F / 360.0F));
        out.writeByte((byte) (int) (this.headYaw * 256.0F / 360.0F));
        out.writeShort((int) (this.motX * 8000.0D));
        out.writeShort((int) (this.motY * 8000.0D));
        out.writeShort((int) (this.motZ * 8000.0D));
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Type {
        CREEPER,
        SKELETON,
        SPIDER,
        GIANT_ZOMBIE,
        ZOMBIE,
        SLIME,
        GHAST,
        ZOMBIE_PIGMAN,
        ENDERMAN,
        CAVE_SPIDER,
        SILVERFISH,
        BLAZE,
        MAGMA_CUBE,
        ENDER_DRAGON,
        WITHER,
        BAT,
        WITCH,
        PIG,
        SHEEP,
        COW,
        CHICKEN,
        SQUID,
        WOLF,
        MOOSHROOM,
        SNOWMAN,
        OCELOT,
        IRON_GOLEM,
        HORSE,
        VILLAGER;
    }
}