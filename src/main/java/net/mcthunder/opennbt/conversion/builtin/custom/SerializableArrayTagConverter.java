package net.mcthunder.opennbt.conversion.builtin.custom;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.custom.SerializableArrayTag;

import java.io.Serializable;

/**
 * A converter that converts between SerializableArrayTag and Serializable[].
 */
public class SerializableArrayTagConverter implements TagConverter<SerializableArrayTag, Serializable[]> {

    @Override
    public Serializable[] convert(SerializableArrayTag tag) {
        return tag.getValue();
    }

    @Override
    public SerializableArrayTag convert(String name, Serializable[] value) {
        return new SerializableArrayTag(name, value);
    }

}
