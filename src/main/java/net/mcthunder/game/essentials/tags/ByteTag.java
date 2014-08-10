package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ByteTag extends Tag {
    private byte value;

    public ByteTag(String name) {
        this(name, 0);
    }

    public ByteTag(String name, byte value) {
        super(name);
        this.value = value;
    }

    public Byte getValue() {
        return Byte.valueOf(this.value);
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = in.readByte();
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeByte(this.value);
    }

    public ByteTag clone() {
        return new ByteTag(getName(), getValue().byteValue());
    }
}