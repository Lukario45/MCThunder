package net.mcthunder.game.essentials;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;


public class TagRegistry {
    private static final Map<Integer, Class<? extends Tag>> idToTag = new HashMap();
    private static final Map<Class<? extends Tag>, Integer> tagToId = new HashMap();

    public static void register(int id, Class<? extends Tag> tag)
            throws TagRegisterException {
        if (idToTag.containsKey(Integer.valueOf(id))) {
            throw new TagRegisterException("Tag ID \"" + id + "\" is already in use.");
        }

        if (tagToId.containsKey(tag)) {
            throw new TagRegisterException("Tag \"" + tag.getSimpleName() + "\" is already registered.");
        }

        idToTag.put(Integer.valueOf(id), tag);
        tagToId.put(tag, Integer.valueOf(id));
    }

    public static Class<? extends Tag> getClassFor(int id) {
        if (!idToTag.containsKey(Integer.valueOf(id))) {
            return null;
        }

        return (Class) idToTag.get(Integer.valueOf(id));
    }

    public static int getIdFor(Class<? extends Tag> clazz) {
        if (!tagToId.containsKey(clazz)) {
            return -1;
        }

        return ((Integer) tagToId.get(clazz)).intValue();
    }

    public static Tag createInstance(int id, String tagName)
            throws TagCreateException {
        Class clazz = (Class) idToTag.get(Integer.valueOf(id));
        if (clazz == null) {
            throw new TagCreateException("Could not find tag with ID \"" + id + "\".");
        }
        try {
            Constructor constructor = clazz.getDeclaredConstructor(new Class[]{String.class});
            constructor.setAccessible(true);
            return (Tag) constructor.newInstance(new Object[]{tagName});
        } catch (Exception e) {
        }
        throw new TagCreateException("Failed to create instance of tag \"" + clazz.getSimpleName() + "\".", e);
    }

    static {
        register(1, ByteTag.class);
        register(2, ShortTag.class);
        register(3, IntTag.class);
        register(4, LongTag.class);
        register(5, FloatTag.class);
        register(6, DoubleTag.class);
        register(7, ByteArrayTag.class);
        register(8, StringTag.class);
        register(9, ListTag.class);
        register(10, CompoundTag.class);
        register(11, IntArrayTag.class);

        register(60, DoubleArrayTag.class);
        register(61, FloatArrayTag.class);
        register(62, LongArrayTag.class);
        register(63, SerializableArrayTag.class);
        register(64, SerializableTag.class);
        register(65, ShortArrayTag.class);
        register(66, StringArrayTag.class);
    }
}