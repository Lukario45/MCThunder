package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class IntTag extends Tag {
    private int value;

    public IntTag(String name) {
        this(name, 0);
    }

    public IntTag(String name, int value) {
        super(name);
        this.value = value;
    }

    public Integer getValue() {
        return Integer.valueOf(this.value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = in.readInt();
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.value);
    }

    public IntTag clone() {
        return new IntTag(getName(), getValue().intValue());
    }
}