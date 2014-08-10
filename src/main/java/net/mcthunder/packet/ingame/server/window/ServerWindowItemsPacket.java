package net.mcthunder.packet.ingame.server.window;

import net.mcthunder.game.essentials.ItemStack;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerWindowItemsPacket
        implements Packet {
    private int windowId;
    private ItemStack[] items;

    private ServerWindowItemsPacket() {
    }

    public ServerWindowItemsPacket(int windowId, ItemStack[] items) {
        this.windowId = windowId;
        this.items = items;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public ItemStack[] getItems() {
        return this.items;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.items = new ItemStack[in.readShort()];
        for (int index = 0; index < this.items.length; index++)
            this.items[index] = NetUtil.readItem(in);
    }

    public void write(NetOut out)
            throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.items.length);
        for (ItemStack item : this.items)
            NetUtil.writeItem(out, item);
    }

    public boolean isPriority() {
        return false;
    }
}