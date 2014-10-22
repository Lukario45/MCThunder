package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Column {
    Chunk[] chunks;
    private int x;
    private int z;

    public Column(long XZ, Chunk[] chunks) {
        this.x = (int) (XZ >> 32);
        this.z = (int) XZ;
        this.chunks = chunks;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public void sendColumns(Player p, Column[] columns) {
        for (Column c : columns) {
            ServerChunkDataPacket packet = new ServerChunkDataPacket(c.getX(), c.getZ(), c.getChunks(), new byte[256]);
            p.getSession().send(packet);
        }
    }

}
