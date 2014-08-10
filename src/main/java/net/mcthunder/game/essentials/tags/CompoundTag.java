package net.mcthunder.game.essentials.tags;

import net.mcthunder.game.essentials.NBTIO;
import net.mcthunder.game.essentials.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.*;


public class CompoundTag extends Tag
        implements Iterable<Tag> {
    private Map<String, Tag> value;

    public CompoundTag(String name) {
        this(name, new LinkedHashMap());
    }

    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);
        this.value = new LinkedHashMap(value);
    }

    public Map<String, Tag> getValue() {
        return new LinkedHashMap(this.value);
    }

    public void setValue(Map<String, Tag> value) {
        this.value = new LinkedHashMap(value);
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public boolean contains(String tagName) {
        return this.value.containsKey(tagName);
    }

    public <T extends Tag> T get(String tagName) {
        return (Tag) this.value.get(tagName);
    }

    public <T extends Tag> T put(T tag) {
        return (Tag) this.value.put(tag.getName(), tag);
    }

    public <T extends Tag> T remove(String tagName) {
        return (Tag) this.value.remove(tagName);
    }

    public Set<String> keySet() {
        return this.value.keySet();
    }

    public Collection<Tag> values() {
        return this.value.values();
    }

    public int size() {
        return this.value.size();
    }

    public void clear() {
        this.value.clear();
    }

    public Iterator<Tag> iterator() {
        return values().iterator();
    }

    public void read(DataInputStream in) throws IOException {
        List tags = new ArrayList();
        try {
            Tag tag;
            while ((tag = NBTIO.readTag(in)) != null)
                tags.add(tag);
        } catch (EOFException e) {
            throw new IOException("Closing EndTag was not found!");
        }

        for (Tag tag : tags)
            put(tag);
    }

    public void write(DataOutputStream out)
            throws IOException {
        for (Tag tag : this.value.values()) {
            NBTIO.writeTag(out, tag);
        }

        out.writeByte(0);
    }

    public CompoundTag clone() {
        Map newMap = new LinkedHashMap();
        for (Map.Entry entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), ((Tag) entry.getValue()).clone());
        }

        return new CompoundTag(getName(), newMap);
    }
}