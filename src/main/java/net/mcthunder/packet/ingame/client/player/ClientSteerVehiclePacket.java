package net.mcthunder.packet.ingame.client.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;


public class ClientSteerVehiclePacket
        implements Packet {
    private float sideways;
    private float forward;
    private boolean jump;
    private boolean dismount;

    private ClientSteerVehiclePacket() {
    }

    public ClientSteerVehiclePacket(float sideways, float forward, boolean jump, boolean dismount) {
        this.sideways = sideways;
        this.forward = forward;
        this.jump = jump;
        this.dismount = dismount;
    }

    public float getSideways() {
        return this.sideways;
    }

    public float getForward() {
        return this.forward;
    }

    public boolean getJumping() {
        return this.jump;
    }

    public boolean getDismounting() {
        return this.dismount;
    }

    public void read(NetIn in) throws IOException {
        this.sideways = in.readFloat();
        this.forward = in.readFloat();
        this.jump = in.readBoolean();
        this.dismount = in.readBoolean();
    }

    public void write(NetOut out) throws IOException {
        out.writeFloat(this.sideways);
        out.writeFloat(this.forward);
        out.writeBoolean(this.jump);
        out.writeBoolean(this.dismount);
    }

    public boolean isPriority() {
        return false;
    }
}