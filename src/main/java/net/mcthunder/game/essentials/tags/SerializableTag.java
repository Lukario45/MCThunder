package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.*;

public class SerializableTag extends Tag {
    private Serializable value;

    public SerializableTag(String name) {
        this(name, Integer.valueOf(0));
    }

    public SerializableTag(String name, Serializable value) {
        super(name);
        this.value = value;
    }

    public Serializable getValue() {
        return this.value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    public void read(DataInputStream in) throws IOException {
        ObjectInputStream str = new ObjectInputStream(in);
        try {
            this.value = ((Serializable) str.readObject());
        } catch (ClassNotFoundException e) {
            throw new IOException("Class not found while reading SerializableTag!", e);
        }
    }

    public void write(DataOutputStream out) throws IOException {
        ObjectOutputStream str = new ObjectOutputStream(out);
        str.writeObject(this.value);
    }

    public SerializableTag clone() {
        return new SerializableTag(getName(), getValue());
    }
}