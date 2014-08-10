package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerCollectItemPacket
        implements Packet {
    private int collectedEntityId;
    private int collectorEntityId;

    private ServerCollectItemPacket() {
    }

    public ServerCollectItemPacket(int collectedEntityId, int collectorEntityId) {
        this.collectedEntityId = collectedEntityId;
        this.collectorEntityId = collectorEntityId;
    }

    public int getCollectedEntityId() {
        return this.collectedEntityId;
    }

    public int getCollectorEntityId() {
        return this.collectorEntityId;
    }

    public void read(NetIn in) throws IOException {
        this.collectedEntityId = in.readInt();
        this.collectorEntityId = in.readInt();
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.collectedEntityId);
        out.writeInt(this.collectorEntityId);
    }

    public boolean isPriority() {
        return false;
    }
}