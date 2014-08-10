package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.game.essentials.EntityMetadata;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityMetadataPacket
        implements Packet {
    private int entityId;
    private EntityMetadata[] metadata;

    private ServerEntityMetadataPacket() {
    }

    public ServerEntityMetadataPacket(int entityId, EntityMetadata[] metadata) {
        this.entityId = entityId;
        this.metadata = metadata;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    public boolean isPriority() {
        return false;
    }
}