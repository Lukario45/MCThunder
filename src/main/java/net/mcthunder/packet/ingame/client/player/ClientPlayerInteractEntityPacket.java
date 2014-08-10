package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientPlayerInteractEntityPacket
        implements Packet {
    private int entityId;
    private Action action;

    private ClientPlayerInteractEntityPacket() {
    }

    public ClientPlayerInteractEntityPacket(int entityId, Action action) {
        this.entityId = entityId;
        this.action = action;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Action getAction() {
        return this.action;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.action = Action.values()[in.readByte()];
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(this.action.ordinal());
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Action {
        INTERACT,
        ATTACK;
    }
}