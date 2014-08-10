package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.NBTIO;
import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringArrayTag extends Tag {
    private String[] value;

    public StringArrayTag(String name) {
        this(name, new String[0]);
    }

    public StringArrayTag(String name, String[] value) {
        super(name);
        this.value = value;
    }

    public String[] getValue() {
        return (String[]) this.value.clone();
    }

    public void setValue(String[] value) {
        if (value == null) {
            return;
        }

        this.value = ((String[]) value.clone());
    }

    public String getValue(int index) {
        return this.value[index];
    }

    public void setValue(int index, String value) {
        this.value[index] = value;
    }

    public int length() {
        return this.value.length;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = new String[in.readInt()];
        for (int index = 0; index < this.value.length; index++) {
            byte[] bytes = new byte[in.readShort()];
            in.readFully(bytes);
            this.value[index] = new String(bytes, NBTIO.CHARSET);
        }
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeInt(this.value.length);
        for (int index = 0; index < this.value.length; index++) {
            byte[] bytes = this.value[index].getBytes(NBTIO.CHARSET);
            out.writeShort(bytes.length);
            out.write(bytes);
        }
    }

    public StringArrayTag clone() {
        return new StringArrayTag(getName(), getValue());
    }
}