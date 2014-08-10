package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DoubleTag extends Tag {
    private double value;

    public DoubleTag(String name) {
        this(name, 0.0D);
    }

    public DoubleTag(String name, double value) {
        super(name);
        this.value = value;
    }

    public Double getValue() {
        return Double.valueOf(this.value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = in.readDouble();
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeDouble(this.value);
    }

    public DoubleTag clone() {
        return new DoubleTag(getName(), getValue().doubleValue());
    }
}