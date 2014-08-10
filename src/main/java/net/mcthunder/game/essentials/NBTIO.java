package net.mcthunder.game.essentials;

import net.mcthunder.game.essentials.tags.CompoundTag;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class NBTIO {
    public static final Charset CHARSET = Charset.forName("UTF-8");

    public static CompoundTag readFile(String path)
            throws IOException {
        return readFile(new File(path));
    }

    public static CompoundTag readFile(File file)
            throws IOException {
        return readFile(file, true);
    }

    public static CompoundTag readFile(String path, boolean compressed)
            throws IOException {
        return readFile(new File(path), compressed);
    }

    public static CompoundTag readFile(File file, boolean compressed)
            throws IOException {
        InputStream in = new FileInputStream(file);
        if (compressed) {
            in = new GZIPInputStream(in);
        }

        Tag tag = readTag(new DataInputStream(in));
        if (!(tag instanceof CompoundTag)) {
            throw new IOException("Root tag is not a CompoundTag!");
        }

        return (CompoundTag) tag;
    }

    public static void writeFile(CompoundTag tag, String path)
            throws IOException {
        writeFile(tag, new File(path));
    }

    public static void writeFile(CompoundTag tag, File file)
            throws IOException {
        writeFile(tag, file, true);
    }

    public static void writeFile(CompoundTag tag, String path, boolean compressed)
            throws IOException {
        writeFile(tag, new File(path), compressed);
    }

    public static void writeFile(CompoundTag tag, File file, boolean compressed)
            throws IOException {
        if (!file.exists()) {
            if ((file.getParentFile() != null) && (!file.getParentFile().exists())) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
        }

        OutputStream out = new FileOutputStream(file);
        if (compressed) {
            out = new GZIPOutputStream(out);
        }

        writeTag(new DataOutputStream(out), tag);
        out.close();
    }

    public static Tag readTag(DataInputStream in)
            throws IOException {
        int id = in.readUnsignedByte();
        if (id == 0) {
            return null;
        }

        byte[] nameBytes = new byte[in.readUnsignedShort()];
        in.readFully(nameBytes);
        String name = new String(nameBytes, CHARSET);
        Tag tag = null;
        try {
            tag = TagRegistry.createInstance(id, name);
        } catch (TagCreateException e) {
            throw new IOException("Failed to create tag.", e);
        }

        tag.read(in);
        return tag;
    }

    public static void writeTag(DataOutputStream out, Tag tag)
            throws IOException {
        byte[] nameBytes = tag.getName().getBytes(CHARSET);
        out.writeByte(TagRegistry.getIdFor(tag.getClass()));
        out.writeShort(nameBytes.length);
        out.write(nameBytes);
        tag.write(out);
    }
}