package net.mcthunder.packet.ingame.server.entity.spawn;

import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.properties.Property;
import net.mcthunder.game.essentials.EntityMetadata;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSpawnPlayerPacket
        implements Packet {
    private int entityId;
    private GameProfile profile;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int currentItem;
    private EntityMetadata[] metadata;

    private ServerSpawnPlayerPacket() {
    }

    public ServerSpawnPlayerPacket(int entityId, GameProfile profile, double x, double y, double z, float yaw, float pitch, int currentItem, EntityMetadata[] metadata) {
        this.entityId = entityId;
        this.profile = profile;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.currentItem = currentItem;
        this.metadata = metadata;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public int getCurrentItem() {
        return this.currentItem;
    }

    public EntityMetadata[] getMetadata() {
        return this.metadata;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readVarInt();
        this.profile = new GameProfile(in.readString(), in.readString());
        int numProperties = in.readVarInt();
        for (int count = 0; count < numProperties; count++) {
            String name = in.readString();
            String value = in.readString();
            String signature = in.readString();
            this.profile.getProperties().put(name, new Property(name, value, signature));
        }

        this.x = (in.readInt() / 32.0D);
        this.y = (in.readInt() / 32.0D);
        this.z = (in.readInt() / 32.0D);
        this.yaw = (in.readByte() * 360 / 256.0F);
        this.pitch = (in.readByte() * 360 / 256.0F);
        this.currentItem = in.readShort();
        this.metadata = NetUtil.readEntityMetadata(in);
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeString(this.profile.getIdAsString());
        out.writeString(this.profile.getName());
        out.writeVarInt(this.profile.getProperties().size());
        for (Property property : this.profile.getProperties().values()) {
            out.writeString(property.getName());
            out.writeString(property.getValue());
            out.writeString(property.getSignature());
        }

        out.writeInt((int) (this.x * 32.0D));
        out.writeInt((int) (this.y * 32.0D));
        out.writeInt((int) (this.z * 32.0D));
        out.writeByte((byte) (int) (this.yaw * 256.0F / 360.0F));
        out.writeByte((byte) (int) (this.pitch * 256.0F / 360.0F));
        out.writeShort(this.currentItem);
        NetUtil.writeEntityMetadata(out, this.metadata);
    }

    public boolean isPriority() {
        return false;
    }
}