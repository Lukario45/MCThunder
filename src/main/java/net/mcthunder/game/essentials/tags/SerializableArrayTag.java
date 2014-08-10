package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.Tag;

import java.io.*;


public class SerializableArrayTag extends Tag {
    private Serializable[] value;

    public SerializableArrayTag(String name) {
        this(name, new Serializable[0]);
    }

    public SerializableArrayTag(String name, Serializable[] value) {
        super(name);
        this.value = value;
    }

    public Serializable[] getValue() {
        return (Serializable[]) this.value.clone();
    }

    public void setValue(Serializable[] value) {
        if (value == null) {
            return;
        }

        this.value = ((Serializable[]) value.clone());
    }

    public Serializable getValue(int index) {
        return this.value[index];
    }

    public void setValue(int index, Serializable value) {
        this.value[index] = value;
    }

    public int length() {
        return this.value.length;
    }

    public void read(DataInputStream in) throws IOException {
        this.value = new Serializable[in.readInt()];
        ObjectInputStream str = new ObjectInputStream(in);
        for (int index = 0; index < this.value.length; index++)
            try {
                this.value[index] = ((Serializable) str.readObject());
            } catch (ClassNotFoundException e) {
                throw new IOException("Class not found while reading SerializableArrayTag!", e);
            }
    }

    public void write(DataOutputStream out)
            throws IOException {
        out.writeInt(this.value.length);
        ObjectOutputStream str = new ObjectOutputStream(out);
        for (int index = 0; index < this.value.length; index++)
            str.writeObject(this.value[index]);
    }

    public SerializableArrayTag clone() {
        return new SerializableArrayTag(getName(), getValue());
    }
}