package net.mcthunder.packet.ingame.server.entity.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerPlayerAbilitiesPacket
        implements Packet {
    private boolean invincible;
    private boolean canFly;
    private boolean flying;
    private boolean creative;
    private float flySpeed;
    private float walkSpeed;

    private ServerPlayerAbilitiesPacket() {
    }

    public ServerPlayerAbilitiesPacket(boolean invincible, boolean canFly, boolean flying, boolean creative, float flySpeed, float walkSpeed) {
        this.invincible = invincible;
        this.canFly = canFly;
        this.flying = flying;
        this.creative = creative;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public boolean getInvincible() {
        return this.invincible;
    }

    public boolean getCanFly() {
        return this.canFly;
    }

    public boolean getFlying() {
        return this.flying;
    }

    public boolean getCreative() {
        return this.creative;
    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public float getWalkSpeed() {
        return this.walkSpeed;
    }

    public void read(NetIn in) throws IOException {
        byte flags = in.readByte();
        this.invincible = ((flags & 0x1) > 0);
        this.canFly = ((flags & 0x2) > 0);
        this.flying = ((flags & 0x4) > 0);
        this.creative = ((flags & 0x8) > 0);
        this.flySpeed = in.readFloat();
        this.walkSpeed = in.readFloat();
    }

    public void write(NetOut out) throws IOException {
        byte flags = 0;
        if (this.invincible) {
            flags = (byte) (flags | 0x1);
        }

        if (this.canFly) {
            flags = (byte) (flags | 0x2);
        }

        if (this.flying) {
            flags = (byte) (flags | 0x4);
        }

        if (this.creative) {
            flags = (byte) (flags | 0x8);
        }

        out.writeByte(flags);
        out.writeFloat(this.flySpeed);
        out.writeFloat(this.walkSpeed);
    }

    public boolean isPriority() {
        return false;
    }
}