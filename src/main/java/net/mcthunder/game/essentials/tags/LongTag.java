package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LongTag extends Tag {
    private long value;

    public LongTag(String name) {
        this(name, 0L);
    }

    public LongTag(String name, long value) {
        super(name);
        this.value = value;
    }

    public Long getValue() {
        return Long.valueOf(this.value);
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = in.readLong();
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeLong(this.value);
    }

    public LongTag clone() {
        return new LongTag(getName(), getValue().longValue());
    }
}