package net.mcthunder.packet.ingame.server.window;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerCloseWindowPacket
        implements Packet {
    private int windowId;

    private ServerCloseWindowPacket() {
    }

    public ServerCloseWindowPacket(int windowId) {
        this.windowId = windowId;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readUnsignedByte();
    }

    public void write(NetOut out) throws IOException, IOException {
        out.writeByte(this.windowId);
    }

    public boolean isPriority() {
        return false;
    }
}