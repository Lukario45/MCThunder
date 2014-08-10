package net.mcthunder.opennbt.conversion.builtin.custom;

import net.mcthunder.opennbt.conversion.TagConverter;
import net.mcthunder.opennbt.tag.builtin.custom.DoubleArrayTag;

/**
 * A converter that converts between DoubleArrayTag and double[].
 */
public class DoubleArrayTagConverter implements TagConverter<DoubleArrayTag, double[]> {

    @Override
    public double[] convert(DoubleArrayTag tag) {
        return tag.getValue();
    }

    @Override
    public DoubleArrayTag convert(String name, double[] value) {
        return new DoubleArrayTag(name, value);
    }

}
