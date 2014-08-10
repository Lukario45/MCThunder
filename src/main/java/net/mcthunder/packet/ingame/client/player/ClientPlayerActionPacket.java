package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientPlayerActionPacket
        implements Packet {
    private int entityId;
    private Action action;
    private int jumpBoost;

    private ClientPlayerActionPacket() {
    }

    public ClientPlayerActionPacket(int entityId, Action action) {
        this(entityId, action, 0);
    }

    public ClientPlayerActionPacket(int entityId, Action action, int jumpBoost) {
        this.entityId = entityId;
        this.action = action;
        this.jumpBoost = jumpBoost;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Action getAction() {
        return this.action;
    }

    public int getJumpBoost() {
        return this.jumpBoost;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.action = Action.values()[(in.readByte() - 1)];
        this.jumpBoost = in.readInt();
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(this.action.ordinal() + 1);
        out.writeInt(this.jumpBoost);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Action {
        CROUCH,
        UNCROUCH,
        LEAVE_BED,
        START_SPRINTING,
        STOP_SPRINTING;
    }
}