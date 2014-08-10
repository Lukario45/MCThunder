package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.game.essentials.ItemStack;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityEquipmentPacket
        implements Packet {
    private int entityId;
    private int slot;
    private ItemStack item;

    private ServerEntityEquipmentPacket() {
    }

    public ServerEntityEquipmentPacket(int entityId, int slot, ItemStack item) {
        this.entityId = entityId;
        this.slot = slot;
        this.item = item;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.slot = in.readShort();
        this.item = NetUtil.readItem(in);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.item);
    }

    public boolean isPriority() {
        return false;
    }
}