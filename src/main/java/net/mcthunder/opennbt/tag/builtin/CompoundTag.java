package net.mcthunder.opennbt.tag.builtin;

import net.mcthunder.opennbt.NBTIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * A compound tag containing other tags.
 */
public class CompoundTag extends Tag implements Iterable<Tag> {

    private Map<String, Tag> value;

    /**
     * Creates a tag with the specified name.
     *
     * @param name The name of the tag.
     */
    public CompoundTag(String name) {
        this(name, new LinkedHashMap<String, Tag>());
    }

    /**
     * Creates a tag with the specified name.
     *
     * @param name  The name of the tag.
     * @param value The value of the tag.
     */
    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);
        this.value = new LinkedHashMap<String, Tag>(value);
    }

    @Override
    public Map<String, Tag> getValue() {
        return new LinkedHashMap<String, Tag>(this.value);
    }

    /**
     * Sets the value of this tag.
     *
     * @param value New value of this tag.
     */
    public void setValue(Map<String, Tag> value) {
        this.value = new LinkedHashMap<String, Tag>(value);
    }

    /**
     * Checks whether the compound tag is empty.
     *
     * @return Whether the compound tag is empty.
     */
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    /**
     * Checks whether the compound tag contains a tag with the specified name.
     *
     * @param tagName Name of the tag to check for.
     * @return Whether the compound tag contains a tag with the specified name.
     */
    public boolean contains(String tagName) {
        return this.value.containsKey(tagName);
    }

    /**
     * Gets the tag with the specified name.
     *
     * @param tagName Name of the tag.
     * @return The tag with the specified name.
     */
    public <T extends Tag> T get(String tagName) {
        return (T) this.value.get(tagName);
    }

    /**
     * Puts the tag into this compound tag.
     *
     * @param tag Tag to put into this compound tag.
     * @return The previous tag associated with its name, or null if there wasn't one.
     */
    public <T extends Tag> T put(T tag) {
        return (T) this.value.put(tag.getName(), tag);
    }

    /**
     * Removes a tag from this compound tag.
     *
     * @param tagName Name of the tag to remove.
     * @return The removed tag.
     */
    public <T extends Tag> T remove(String tagName) {
        return (T) this.value.remove(tagName);
    }

    /**
     * Gets a set of keys in this compound tag.
     *
     * @return The compound tag's key set.
     */
    public Set<String> keySet() {
        return this.value.keySet();
    }

    /**
     * Gets a collection of tags in this compound tag.
     *
     * @return This compound tag's tags.
     */
    public Collection<Tag> values() {
        return this.value.values();
    }

    /**
     * Gets the number of tags in this compound tag.
     *
     * @return This compound tag's size.
     */
    public int size() {
        return this.value.size();
    }

    /**
     * Clears all tags from this compound tag.
     */
    public void clear() {
        this.value.clear();
    }

    @Override
    public Iterator<Tag> iterator() {
        return this.values().iterator();
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        List<Tag> tags = new ArrayList<Tag>();
        try {
            Tag tag;
            while ((tag = NBTIO.readTag(in)) != null) {
                tags.add(tag);
            }
        } catch (EOFException e) {
            throw new IOException("Closing EndTag was not found!");
        }

        for (Tag tag : tags) {
            this.put(tag);
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        for (Tag tag : this.value.values()) {
            NBTIO.writeTag(out, tag);
        }

        out.writeByte(0);
    }

    @Override
    public CompoundTag clone() {
        Map<String, Tag> newMap = new LinkedHashMap<String, Tag>();
        for (Entry<String, Tag> entry : this.value.entrySet()) {
            newMap.put(entry.getKey(), entry.getValue().clone());
        }

        return new CompoundTag(this.getName(), newMap);
    }

}
