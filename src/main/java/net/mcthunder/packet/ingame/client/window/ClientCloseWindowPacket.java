package net.mcthunder.packet.ingame.client.window;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientCloseWindowPacket
        implements Packet {
    private int windowId;

    private ClientCloseWindowPacket() {
    }

    public ClientCloseWindowPacket(int windowId) {
        this.windowId = windowId;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readByte();
    }

    public void write(NetOut out) throws IOException, IOException {
        out.writeByte(this.windowId);
    }

    public boolean isPriority() {
        return false;
    }
}