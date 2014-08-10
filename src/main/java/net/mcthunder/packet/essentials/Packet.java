package net.mcthunder.packet.essentials;

import java.io.IOException;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface Packet {
    public abstract void read(NetIn paramNetInput)
            throws IOException;

    public abstract void write(NetOut paramNetOutput)
            throws IOException;

    public abstract boolean isPriority();
}
