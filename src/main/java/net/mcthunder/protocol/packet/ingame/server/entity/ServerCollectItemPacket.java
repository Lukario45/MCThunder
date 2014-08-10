package net.mcthunder.protocol.packet.ingame.server.entity;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ServerCollectItemPacket implements Packet {

    private int collectedEntityId;
    private int collectorEntityId;

    @SuppressWarnings("unused")
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

    @Override
    public void read(NetInput in) throws IOException {
        this.collectedEntityId = in.readInt();
        this.collectorEntityId = in.readInt();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.collectedEntityId);
        out.writeInt(this.collectorEntityId);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
