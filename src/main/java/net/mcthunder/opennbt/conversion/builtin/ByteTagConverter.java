package net.mcthunder.opennbt.conversion.builtin;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.ByteTag;

/**
 * A converter that converts between ByteTag and byte.
 */
public class ByteTagConverter implements TagConverter<ByteTag, Byte> {

    @Override
    public Byte convert(ByteTag tag) {
        return tag.getValue();
    }

    @Override
    public ByteTag convert(String name, Byte value) {
        return new ByteTag(name, value);
    }

}
