package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityTeleportPacket
        implements Packet {
    protected int entityId;
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;

    private ServerEntityTeleportPacket() {
    }

    public ServerEntityTeleportPacket(int entityId, double x, double y, double z, float yaw, float pitch) {
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public int getEntityId() {
        return this.entityId;
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

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.x = (in.readInt() / 32.0D);
        this.y = (in.readInt() / 32.0D);
        this.z = (in.readInt() / 32.0D);
        this.yaw = (in.readByte() * 360 / 256.0F);
        this.pitch = (in.readByte() * 360 / 256.0F);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt((int) (this.x * 32.0D));
        out.writeInt((int) (this.y * 32.0D));
        out.writeInt((int) (this.z * 32.0D));
        out.writeByte((byte) (int) (this.yaw * 256.0F / 360.0F));
        out.writeByte((byte) (int) (this.pitch * 256.0F / 360.0F));
    }

    public boolean isPriority() {
        return false;
    }
}