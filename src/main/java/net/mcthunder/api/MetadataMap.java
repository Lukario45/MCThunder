package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.Rotation;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataMap {
    private Map<Integer, EntityMetadata> metadata = new HashMap<>();
    private List<EntityMetadata> changes = new ArrayList<>();

    public Object getMetadata(int id) {
        EntityMetadata metadata = this.metadata.get(id);
        return metadata != null ? metadata.getValue() : null;
    }

    public void setMetadata(int id, EntityMetadata meta) {
        if (meta == null)
            throw new IllegalArgumentException("Cannot set a metadata value to null.");
        EntityMetadata old = this.metadata.get(id);
        this.metadata.put(id, meta);
        if (!meta.equals(old))
            this.changes.add(meta);
    }

    public void setMetadata(int id, Object value) {
        if (value == null)
            throw new IllegalArgumentException("Cannot set a metadata value to null.");

        MetadataType type;
        if (value instanceof Byte)
            type = MetadataType.BYTE;
        else if (value instanceof Short)
            type = MetadataType.SHORT;
        else if (value instanceof Integer)
            type = MetadataType.INT;
        else if (value instanceof Float)
            type = MetadataType.FLOAT;
        else if (value instanceof String)
            type = MetadataType.STRING;
        else if (value instanceof ItemStack)
            type = MetadataType.ITEM;
        else if (value instanceof Position)
            type = MetadataType.POSITION;
        else if (value instanceof Rotation)
            type = MetadataType.ROTATION;
        else
            throw new IllegalArgumentException("Metadata value \"" + value + "\" has an unsupported type.");

        Object old = this.getMetadata(id);
        EntityMetadata metadata = new EntityMetadata(id, type, value);
        this.metadata.put(id, metadata);
        if (!value.equals(old))
            this.changes.add(metadata);
    }

    public Number getNumber(int id) {
        Object value = this.getMetadata(id);
        return value == null ? 0 : (Number) value;
    }

    public boolean getBit(int id, int bit) {
        return (this.getNumber(id).byteValue() & bit) != 0;
    }

    public void setBit(int id, int bit, boolean value) {
        this.setMetadata(id, (byte) (value ? (this.getNumber(id).byteValue() | bit) : (this.getNumber(id).byteValue() & ~bit)));
    }

    public EntityMetadata[] getMetadataArray() {
        return this.metadata.values().toArray(new EntityMetadata[this.metadata.size()]);
    }

    public EntityMetadata[] getChanges() {
        if (this.changes.isEmpty())
            return null;

        EntityMetadata changes[] = this.changes.toArray(new EntityMetadata[this.changes.size()]);
        this.changes.clear();
        return changes;
    }
}