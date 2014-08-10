package net.mcthunder.packet.ingame.client.window;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientEnchantItemPacket
        implements Packet {
    private int windowId;
    private int enchantment;

    private ClientEnchantItemPacket() {
    }

    public ClientEnchantItemPacket(int windowId, int enchantment) {
        this.windowId = windowId;
        this.enchantment = enchantment;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getEnchantment() {
        return this.enchantment;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readByte();
        this.enchantment = in.readByte();
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.windowId);
        out.writeByte(this.enchantment);
    }

    public boolean isPriority() {
        return false;
    }
}