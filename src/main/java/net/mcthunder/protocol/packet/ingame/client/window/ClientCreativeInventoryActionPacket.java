package net.mcthunder.protocol.packet.ingame.client.window;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;
import net.mcthunder.protocol.data.game.ItemStack;
import net.mcthunder.protocol.util.NetUtil;

import java.io.IOException;

public class ClientCreativeInventoryActionPacket implements Packet {

    private int slot;
    private ItemStack clicked;

    @SuppressWarnings("unused")
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

    @Override
    public void read(NetInput in) throws IOException {
        this.slot = in.readShort();
        this.clicked = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.clicked);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
