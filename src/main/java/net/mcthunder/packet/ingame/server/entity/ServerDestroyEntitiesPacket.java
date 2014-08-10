package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerDestroyEntitiesPacket
        implements Packet {
    private int[] entityIds;

    private ServerDestroyEntitiesPacket() {
    }

    public ServerDestroyEntitiesPacket(int[] entityIds) {
        this.entityIds = entityIds;
    }

    public int[] getEntityIds() {
        return this.entityIds;
    }

    public void read(NetIn in) throws IOException {
        this.entityIds = new int[in.readByte()];
        for (int index = 0; index < this.entityIds.length; index++)
            this.entityIds[index] = in.readInt();
    }

    public void write(NetOut out)
            throws IOException {
        out.writeByte(this.entityIds.length);
        for (int entityId : this.entityIds)
            out.writeInt(entityId);
    }

    public boolean isPriority() {
        return false;
    }
}