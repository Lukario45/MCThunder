package net.mcthunder.opennbt.conversion.builtin.custom;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.custom.StringArrayTag;

/**
 * A converter that converts between StringArrayTag and String[].
 */
public class StringArrayTagConverter implements TagConverter<StringArrayTag, String[]> {

    @Override
    public String[] convert(StringArrayTag tag) {
        return tag.getValue();
    }

    @Override
    public StringArrayTag convert(String name, String[] value) {
        return new StringArrayTag(name, value);
    }

}
