package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientPlayerAnimationPacket
        implements Packet {
    private int entityId;
    private Animation animation;

    private ClientPlayerAnimationPacket() {
    }

    public ClientPlayerAnimationPacket(int entityId, Animation animation) {
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
        this.entityId = in.readInt();
        this.animation = Animation.values()[(in.readByte() - 1)];
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(this.animation.ordinal() + 1);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Animation {
        SWING_ARM;
    }
}