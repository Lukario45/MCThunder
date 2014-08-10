package net.mcthunder.apis;

import java.io.IOException;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface NetIn {
    public abstract boolean readBoolean()
            throws IOException;

    public abstract byte readByte()
            throws IOException;

    public abstract int readUnsignedByte()
            throws IOException;

    public abstract short readShort()
            throws IOException;

    public abstract int readUnsignedShort()
            throws IOException;

    public abstract char readChar()
            throws IOException;

    public abstract int readInt()
            throws IOException;

    public abstract int readVarInt()
            throws IOException;

    public abstract long readLong()
            throws IOException;

    public abstract float readFloat()
            throws IOException;

    public abstract double readDouble()
            throws IOException;

    public abstract byte[] readPrefixedBytes()
            throws IOException;

    public abstract byte[] readBytes(int paramInt)
            throws IOException;

    public abstract int readBytes(byte[] paramArrayOfByte)
            throws IOException;

    public abstract int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
            throws IOException;

    public abstract String readString()
            throws IOException;

    public abstract int available()
            throws IOException;
}
