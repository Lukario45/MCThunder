package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityMovementPacket
        implements Packet {
    protected int entityId;
    protected double moveX;
    protected double moveY;
    protected double moveZ;
    protected float yaw;
    protected float pitch;
    protected boolean pos = false;
    protected boolean rot = false;

    protected ServerEntityMovementPacket() {
    }

    public ServerEntityMovementPacket(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public double getMovementX() {
        return this.moveX;
    }

    public double getMovementY() {
        return this.moveY;
    }

    public double getMovementZ() {
        return this.moveZ;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        if (this.pos) {
            this.moveX = (in.readByte() / 32.0D);
            this.moveY = (in.readByte() / 32.0D);
            this.moveZ = (in.readByte() / 32.0D);
        }

        if (this.rot) {
            this.yaw = (in.readByte() * 360 / 256.0F);
            this.pitch = (in.readByte() * 360 / 256.0F);
        }
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        if (this.pos) {
            out.writeByte((int) (this.moveX * 32.0D));
            out.writeByte((int) (this.moveY * 32.0D));
            out.writeByte((int) (this.moveZ * 32.0D));
        }

        if (this.rot) {
            out.writeByte((byte) (int) (this.yaw * 256.0F / 360.0F));
            out.writeByte((byte) (int) (this.pitch * 256.0F / 360.0F));
        }
    }

    public boolean isPriority() {
        return false;
    }
}