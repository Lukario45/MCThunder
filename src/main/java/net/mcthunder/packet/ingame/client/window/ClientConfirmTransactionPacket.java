package net.mcthunder.packet.ingame.client.window;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientConfirmTransactionPacket
        implements Packet {
    private int windowId;
    private int actionId;
    private boolean accepted;

    private ClientConfirmTransactionPacket() {
    }

    public ClientConfirmTransactionPacket(int windowId, int actionId, boolean accepted) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.accepted = accepted;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getActionId() {
        return this.actionId;
    }

    public boolean getAccepted() {
        return this.accepted;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readByte();
        this.actionId = in.readShort();
        this.accepted = in.readBoolean();
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.actionId);
        out.writeBoolean(this.accepted);
    }

    public boolean isPriority() {
        return false;
    }
}