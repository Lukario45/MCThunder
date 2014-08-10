package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FloatTag extends Tag {
    private float value;

    public FloatTag(String name) {
        this(name, 0.0F);
    }

    public FloatTag(String name, float value) {
        super(name);
        this.value = value;
    }

    public Float getValue() {
        return Float.valueOf(this.value);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = in.readFloat();
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeFloat(this.value);
    }

    public FloatTag clone() {
        return new FloatTag(getName(), getValue().floatValue());
    }
}