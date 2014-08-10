package net.mcthunder.protocol.packet.ingame.server.entity;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ServerAnimationPacket implements Packet {

    private int entityId;
    private Animation animation;

    @SuppressWarnings("unused")
    private ServerAnimationPacket() {
    }

    public ServerAnimationPacket(int entityId, Animation animation) {
        this.entityId = entityId;
        this.animation = animation;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.animation = Animation.values()[in.readByte()];
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(this.animation.ordinal());
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    public static enum Animation {
        SWING_ARM,
        DAMAGE,
        LEAVE_BED,
        EAT_FOOD,
        CRITICAL_HIT,
        ENCHANTMENT_CRITICAL_HIT;
    }

}
