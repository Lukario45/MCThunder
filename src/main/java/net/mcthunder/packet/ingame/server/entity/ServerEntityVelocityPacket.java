package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityVelocityPacket
        implements Packet {
    private int entityId;
    private double motX;
    private double motY;
    private double motZ;

    private ServerEntityVelocityPacket() {
    }

    public ServerEntityVelocityPacket(int entityId, double motX, double motY, double motZ) {
        this.entityId = entityId;
        this.motX = motX;
        this.motY = motY;
        this.motZ = motZ;
    }

    public int getEntityId() {
        return this.entityId;
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
        this.entityId = in.readInt();
        this.motX = (in.readShort() / 8000.0D);
        this.motY = (in.readShort() / 8000.0D);
        this.motZ = (in.readShort() / 8000.0D);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeShort((int) (this.motX * 8000.0D));
        out.writeShort((int) (this.motY * 8000.0D));
        out.writeShort((int) (this.motZ * 8000.0D));
    }

    public boolean isPriority() {
        return false;
    }
}