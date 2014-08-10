package net.mcthunder.packet.ingame.client.window;

import net.mcthunder.game.essentials.ItemStack;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientCreativeInventoryActionPacket
        implements Packet {
    private int slot;
    private ItemStack clicked;

    private ClientCreativeInventoryActionPacket() {
    }

    public ClientCreativeInventoryActionPacket(int slot, ItemStack clicked) {
        this.slot = slot;
        this.clicked = clicked;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getClickedItem() {
        return this.clicked;
    }

    public void read(NetIn in) throws IOException {
        this.slot = in.readShort();
        this.clicked = NetUtil.readItem(in);
    }

    public void write(NetOut out) throws IOException {
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.clicked);
    }

    public boolean isPriority() {
        return false;
    }
}