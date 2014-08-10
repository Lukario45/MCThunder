package net.mcthunder.packet.ingame.server.window;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerOpenWindowPacket
        implements Packet {
    private int windowId;
    private Type type;
    private String name;
    private int slots;
    private boolean useName;
    private int ownerEntityId;

    private ServerOpenWindowPacket() {
    }

    public ServerOpenWindowPacket(int windowId, Type type, String name, int slots, boolean useName) {
        this(windowId, type, name, slots, useName, 0);
    }

    public ServerOpenWindowPacket(int windowId, Type type, String name, int slots, boolean useName, int ownerEntityId) {
        this.windowId = windowId;
        this.type = type;
        this.name = name;
        this.slots = slots;
        this.useName = useName;
        this.ownerEntityId = ownerEntityId;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public Type getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public int getSlots() {
        return this.slots;
    }

    public boolean getUseName() {
        return this.useName;
    }

    public int getOwnerEntityId() {
        return this.ownerEntityId;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readUnsignedByte();
        this.type = Type.values()[in.readUnsignedByte()];
        this.name = in.readString();
        this.slots = in.readUnsignedByte();
        this.useName = in.readBoolean();
        if (this.type == Type.HORSE_INVENTORY)
            this.ownerEntityId = in.readInt();
    }

    public void write(NetOut out)
            throws IOException {
        out.writeByte(this.windowId);
        out.writeByte(this.type.ordinal());
        out.writeString(this.name);
        out.writeByte(this.slots);
        out.writeBoolean(this.useName);
        if (this.type == Type.HORSE_INVENTORY)
            out.writeInt(this.ownerEntityId);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Type {
        CHEST,
        CRAFTING_TABLE,
        FURNACE,
        DISPENSER,
        ENCHANTMENT_TABLE,
        BREWING_STAND,
        VILLAGER_TRADE,
        BEACON,
        ANVIL,
        HOPPER,
        DROPPER,
        HORSE_INVENTORY;
    }
}