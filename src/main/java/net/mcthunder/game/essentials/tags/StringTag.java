package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.NBTIO;
import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class StringTag extends Tag {

    private String value;

    public StringTag(String name) {
        this(name, "");
    }

    public StringTag(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        byte[] bytes = new byte[in.readShort()];
        in.readFully(bytes);
        this.value = new String(bytes, NBTIO.CHARSET);
    }

    public void write(DataOutputStream out) throws IOException {
        byte[] bytes = this.value.getBytes(NBTIO.CHARSET);
        out.writeShort(bytes.length);
        out.write(bytes);
    }

    public StringTag clone() {
        return new StringTag(getName(), getValue());
    }
}