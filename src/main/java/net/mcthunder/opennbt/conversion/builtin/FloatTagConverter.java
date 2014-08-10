package net.mcthunder.opennbt.conversion.builtin;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.FloatTag;

/**
 * A converter that converts between FloatTag and float.
 */
public class FloatTagConverter implements TagConverter<FloatTag, Float> {

    @Override
    public Float convert(FloatTag tag) {
        return tag.getValue();
    }

    @Override
    public FloatTag convert(String name, Float value) {
        return new FloatTag(name, value);
    }

}
