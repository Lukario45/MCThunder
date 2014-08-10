package net.mcthunder.packet.essentials;

import java.io.IOException;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface PacketHead {
    public abstract boolean isLengthVariable();

    public abstract int getLengthSize();

    public abstract int getLengthSize(int paramInt);

    public abstract int readLength(NetIn paramNetInput, int paramInt)
            throws IOException;

    public abstract void writeLength(NetOut paramNetOutput, int paramInt)
            throws IOException;

    public abstract int readPacketId(NetIn paramNetInput)
            throws IOException;

    public abstract void writePacketId(NetOut paramNetOutput, int paramInt)
            throws IOException;
}
