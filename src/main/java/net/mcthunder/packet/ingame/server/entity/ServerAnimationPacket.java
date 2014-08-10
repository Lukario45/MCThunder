package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerAnimationPacket
        implements Packet {
    private int entityId;
    private Animation animation;

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

    public void read(NetIn in) throws IOException {
        this.entityId = in.readVarInt();
        this.animation = Animation.values()[in.readByte()];
    }

    public void write(NetOut out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeByte(this.animation.ordinal());
    }

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