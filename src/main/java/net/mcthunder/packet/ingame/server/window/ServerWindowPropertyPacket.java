package net.mcthunder.packet.ingame.server.window;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerWindowPropertyPacket
        implements Packet {
    private int windowId;
    private int property;
    private int value;

    private ServerWindowPropertyPacket() {
    }

    public ServerWindowPropertyPacket(int windowId, int property, int value) {
        this.windowId = windowId;
        this.property = property;
        this.value = value;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getProperty() {
        return this.property;
    }

    public int getValue() {
        return this.value;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.property = in.readShort();
        this.value = in.readShort();
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.property);
        out.writeShort(this.value);
    }

    public boolean isPriority() {
        return false;
    }

    public static class Property {
        public static final int FURNACE_PROGRESS = 0;
        public static final int FURNACE_FUEL = 1;
        public static final int ENCHANTMENT_SLOT_1 = 0;
        public static final int ENCHANTMENT_SLOT_2 = 1;
        public static final int ENCHANTMENT_SLOT_3 = 2;
    }
}