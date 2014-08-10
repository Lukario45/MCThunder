package net.mcthunder.opennbt.conversion.builtin;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.LongTag;

/**
 * A converter that converts between LongTag and long.
 */
public class LongTagConverter implements TagConverter<LongTag, Long> {

    @Override
    public Long convert(LongTag tag) {
        return tag.getValue();
    }

    @Override
    public LongTag convert(String name, Long value) {
        return new LongTag(name, value);
    }

}
