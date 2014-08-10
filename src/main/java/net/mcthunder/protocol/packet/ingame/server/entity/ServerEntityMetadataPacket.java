package net.mcthunder.protocol.packet.ingame.server.entity;

import net.mcthunder.protocol.data.game.EntityMetadata;
import net.mcthunder.protocol.util.NetUtil;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerEntityMetadataPacket implements Packet {

    private int entityId;
    private EntityMetadata metadata[];

    @SuppressWarnings("unused")
    private ServerEntityMetadataPacket() {
    }

    public ServerEntityMetadataPacket(int entityId, EntityMetadata metadata[]) {
        this.entityId = entityId;
        this.metadata = metadata;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.entityId);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    @Override
    public boolean isPriority() {
        return false;
    }

}
