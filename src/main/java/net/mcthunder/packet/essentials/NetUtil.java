package net.mcthunder.packet.essentials;

import net.mcthunder.game.essentials.*;
import net.mcthunder.game.essentials.tags.CompoundTag;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class NetUtil {
    public static CompoundTag readNBT(NetIn in)
            throws IOException {
        short length = in.readShort();
        if (length < 0) {
            return null;
        }
        return (CompoundTag) NBTIO.readTag(new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(in.readBytes(length)))));
    }

    public static void writeNBT(NetOut out, CompoundTag tag) throws IOException {
        if (tag == null) {
            out.writeShort(-1);
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(output);
            NBTIO.writeTag(new DataOutputStream(gzip), tag);
            gzip.close();
            output.close();
            byte[] bytes = output.toByteArray();
            out.writeShort((short) bytes.length);
            out.writeBytes(bytes);
        }
    }

    public static ItemStack readItem(NetIn in) throws IOException {
        short item = in.readShort();
        if (item < 0) {
            return null;
        }
        return new ItemStack(item, in.readByte(), in.readShort(), readNBT(in));
    }

    public static void writeItem(NetOut out, ItemStack item) throws IOException {
        if (item == null) {
            out.writeShort(-1);
        } else {
            out.writeShort(item.getId());
            out.writeByte(item.getAmount());
            out.writeShort(item.getData());
            writeNBT(out, item.getNBT());
        }
    }

    public static EntityMetadata[] readEntityMetadata(NetIn in) throws IOException {
        List ret = new ArrayList();
        byte b;
        while ((b = in.readByte()) != 127) {
            int typeId = (b & 0xE0) >> 5;
            EntityMetadata.Type type = EntityMetadata.Type.values()[typeId];
            int id = b & 0x1F;
            Object value = null;
            switch (type.ordinal()) {
                case 1:
                    value = Byte.valueOf(in.readByte());
                    break;
                case 2:
                    value = Short.valueOf(in.readShort());
                    break;
                case 3:
                    value = Integer.valueOf(in.readInt());
                    break;
                case 4:
                    value = Float.valueOf(in.readFloat());
                    break;
                case 5:
                    value = in.readString();
                    break;
                case 6:
                    value = readItem(in);
                    break;
                case 7:
                    value = new Coordinates(in.readInt(), in.readInt(), in.readInt());
                    break;
                default:
                    throw new IOException("Unknown metadata type id: " + typeId);
            }

            ret.add(new EntityMetadata(id, type, value));
        }

        return (EntityMetadata[]) ret.toArray(new EntityMetadata[ret.size()]);
    }

    public static void writeEntityMetadata(NetOut out, EntityMetadata[] metadata) throws IOException {
        for (EntityMetadata meta : metadata) {
            int id = (meta.getType().ordinal() << 5 | meta.getId() & 0x1F) & 0xFF;
            out.writeByte(id);
            switch (meta.getType().ordinal()) {
                case 1:
                    out.writeByte(((Byte) meta.getValue()).byteValue());
                    break;
                case 2:
                    out.writeShort(((Short) meta.getValue()).shortValue());
                    break;
                case 3:
                    out.writeInt(((Integer) meta.getValue()).intValue());
                    break;
                case 4:
                    out.writeFloat(((Float) meta.getValue()).floatValue());
                    break;
                case 5:
                    out.writeString((String) meta.getValue());
                    break;
                case 6:
                    writeItem(out, (ItemStack) meta.getValue());
                    break;
                case 7:
                    Coordinates coords = (Coordinates) meta.getValue();
                    out.writeInt(coords.getX());
                    out.writeInt(coords.getY());
                    out.writeInt(coords.getZ());
                    break;
                default:
                    throw new IOException("Unmapped metadata type: " + meta.getType());
            }
        }

        out.writeByte(127);
    }

    public static ParsedChunkData dataToChunks(NetworkChunkData data) {
        Chunk[] chunks = new Chunk[16];
        int pos = 0;
        int expected = 0;
        boolean sky = false;

        for (int pass = 0; pass < 5; pass++) {
            for (int ind = 0; ind < 16; ind++) {
                if ((data.getMask() & 1 << ind) != 0) {
                    if (pass == 0) {
                        expected += 10240;
                        if ((data.getExtendedMask() & 1 << ind) != 0) {
                            expected += 2048;
                        }
                    }

                    if (pass == 1) {
                        chunks[ind] = new Chunk((sky) || (data.hasSkyLight()), (data.getExtendedMask() & 1 << ind) != 0);
                        ByteArray3d blocks = chunks[ind].getBlocks();
                        System.arraycopy(data.getData(), pos, blocks.getData(), 0, blocks.getData().length);
                        pos += blocks.getData().length;
                    }

                    if (pass == 2) {
                        NibbleArray3d metadata = chunks[ind].getMetadata();
                        System.arraycopy(data.getData(), pos, metadata.getData(), 0, metadata.getData().length);
                        pos += metadata.getData().length;
                    }

                    if (pass == 3) {
                        NibbleArray3d blocklight = chunks[ind].getBlockLight();
                        System.arraycopy(data.getData(), pos, blocklight.getData(), 0, blocklight.getData().length);
                        pos += blocklight.getData().length;
                    }

                    if ((pass == 4) && ((sky) || (data.hasSkyLight()))) {
                        NibbleArray3d skylight = chunks[ind].getSkyLight();
                        System.arraycopy(data.getData(), pos, skylight.getData(), 0, skylight.getData().length);
                        pos += skylight.getData().length;
                    }
                }

                if ((pass != 5) ||
                        ((data.getExtendedMask() & 1 << ind) == 0)) continue;
                if (chunks[ind] == null) {
                    pos += 2048;
                } else {
                    NibbleArray3d extended = chunks[ind].getExtendedBlocks();
                    System.arraycopy(data.getData(), pos, extended.getData(), 0, extended.getData().length);
                    pos += extended.getData().length;
                }

            }

            if ((pass != 0) ||
                    (data.getData().length < expected)) continue;
            sky = true;
        }

        byte[] biomeData = null;
        if (data.isFullChunk()) {
            biomeData = new byte[256];
            System.arraycopy(data.getData(), pos, biomeData, 0, biomeData.length);
            pos += biomeData.length;
        }

        return new ParsedChunkData(chunks, biomeData);
    }

    public static NetworkChunkData chunksToData(ParsedChunkData chunks) {
        int chunkMask = 0;
        int extendedChunkMask = 0;
        boolean fullChunk = chunks.getBiomes() != null;
        boolean sky = false;
        int length = fullChunk ? chunks.getBiomes().length : 0;
        byte[] data = null;
        int pos = 0;

        for (int pass = 0; pass < 6; pass++) {
            for (int ind = 0; ind < chunks.getChunks().length; ind++) {
                Chunk chunk = chunks.getChunks()[ind];
                if ((chunk != null) && ((!fullChunk) || (!chunk.isEmpty()))) {
                    if (pass == 0) {
                        chunkMask |= 1 << ind;
                        if (chunk.getExtendedBlocks() != null) {
                            extendedChunkMask |= 1 << ind;
                        }

                        length += chunk.getBlocks().getData().length;
                        length += chunk.getMetadata().getData().length;
                        length += chunk.getBlockLight().getData().length;
                        if (chunk.getSkyLight() != null) {
                            length += chunk.getSkyLight().getData().length;
                        }

                        if (chunk.getExtendedBlocks() != null) {
                            length += chunk.getExtendedBlocks().getData().length;
                        }
                    }

                    if (pass == 1) {
                        ByteArray3d blocks = chunk.getBlocks();
                        System.arraycopy(blocks.getData(), 0, data, pos, blocks.getData().length);
                        pos += blocks.getData().length;
                    }

                    if (pass == 2) {
                        byte[] meta = chunk.getMetadata().getData();
                        System.arraycopy(meta, 0, data, pos, meta.length);
                        pos += meta.length;
                    }

                    if (pass == 3) {
                        byte[] blocklight = chunk.getBlockLight().getData();
                        System.arraycopy(blocklight, 0, data, pos, blocklight.length);
                        pos += blocklight.length;
                    }

                    if ((pass == 4) && (chunk.getSkyLight() != null)) {
                        byte[] skylight = chunk.getSkyLight().getData();
                        System.arraycopy(skylight, 0, data, pos, skylight.length);
                        pos += skylight.length;
                        sky = true;
                    }

                    if ((pass == 5) && (chunk.getExtendedBlocks() != null)) {
                        byte[] extended = chunk.getExtendedBlocks().getData();
                        System.arraycopy(extended, 0, data, pos, extended.length);
                        pos += extended.length;
                    }
                }
            }

            if (pass == 0) {
                data = new byte[length];
            }

        }

        if (fullChunk) {
            System.arraycopy(chunks.getBiomes(), 0, data, pos, chunks.getBiomes().length);
            pos += chunks.getBiomes().length;
        }

        return new NetworkChunkData(chunkMask, extendedChunkMask, fullChunk, sky, data);
    }
}