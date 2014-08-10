package net.mcthunder.protocol.packet.ingame.server.entity;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;
import net.mcthunder.protocol.data.game.Attribute;
import net.mcthunder.protocol.data.game.AttributeModifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerEntityPropertiesPacket implements Packet {

    private int entityId;
    private List<Attribute> attributes;

    @SuppressWarnings("unused")
    private ServerEntityPropertiesPacket() {
    }

    public ServerEntityPropertiesPacket(int entityId, List<Attribute> attributes) {
        this.entityId = entityId;
        this.attributes = attributes;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();
        this.attributes = new ArrayList<Attribute>();
        int length = in.readInt();
        for (int index = 0; index < length; index++) {
            String key = in.readString();
            double value = in.readDouble();
            List<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
            short len = in.readShort();
            for (int ind = 0; ind < len; ind++) {
                modifiers.add(new AttributeModifier(new UUID(in.readLong(), in.readLong()), in.readDouble(), in.readByte()));
            }

            this.attributes.add(new Attribute(key, value, modifiers));
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        out.writeInt(this.attributes.size());
        for (Attribute attribute : this.attributes) {
            out.writeString(attribute.getKey());
            out.writeDouble(attribute.getValue());
            out.writeShort(attribute.getModifiers().size());
            for (AttributeModifier modifier : attribute.getModifiers()) {
                out.writeLong(modifier.getUUID().getMostSignificantBits());
                out.writeLong(modifier.getUUID().getLeastSignificantBits());
                out.writeDouble(modifier.getAmount());
                out.writeByte(modifier.getOperation());
            }
        }
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
