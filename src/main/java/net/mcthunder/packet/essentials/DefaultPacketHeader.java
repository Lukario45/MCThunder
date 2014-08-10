package net.mcthunder.packet.essentials;

import java.io.IOException;

public class DefaultPacketHeader
        implements PacketHead {
    private static int varintLength(int i) {
        if ((i & 0xFFFFFF80) == 0)
            return 1;
        if ((i & 0xFFFFC000) == 0)
            return 2;
        if ((i & 0xFFE00000) == 0)
            return 3;
        if ((i & 0xF0000000) == 0) {
            return 4;
        }
        return 5;
    }

    public boolean isLengthVariable() {
        return true;
    }

    public int getLengthSize() {
        return 5;
    }

    public int getLengthSize(int length) {
        return varintLength(length);
    }

    public int readLength(NetIn in, int available) throws IOException {
        return in.readVarInt();
    }

    public void writeLength(NetOut out, int length) throws IOException {
        out.writeVarInt(length);
    }

    public int readPacketId(NetIn in) throws IOException {
        return in.readVarInt();
    }

    public void writePacketId(NetOut out, int packetId) throws IOException, IOException {
        out.writeVarInt(packetId);
    }
}