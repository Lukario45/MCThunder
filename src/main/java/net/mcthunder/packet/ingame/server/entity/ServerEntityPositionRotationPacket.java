package net.mcthunder.packet.ingame.server.entity;

public class ServerEntityPositionRotationPacket extends ServerEntityMovementPacket {
    protected ServerEntityPositionRotationPacket() {
        this.pos = true;
        this.rot = true;
    }

    public ServerEntityPositionRotationPacket(int entityId, double moveX, double moveY, double moveZ, float yaw, float pitch) {
        super(entityId);
        this.pos = true;
        this.rot = true;
        this.moveX = moveX;
        this.moveY = moveY;
        this.moveZ = moveZ;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}