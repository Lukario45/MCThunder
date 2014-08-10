package net.mcthunder.opennbt.conversion.builtin;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.IntTag;

/**
 * A converter that converts between IntTag and int.
 */
public class IntTagConverter implements TagConverter<IntTag, Integer> {

    @Override
    public Integer convert(IntTag tag) {
        return tag.getValue();
    }

    @Override
    public IntTag convert(String name, Integer value) {
        return new IntTag(name, value);
    }

}
