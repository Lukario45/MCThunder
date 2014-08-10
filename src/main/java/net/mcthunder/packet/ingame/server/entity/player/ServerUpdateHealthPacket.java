package net.mcthunder.packet.ingame.server.entity.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerUpdateHealthPacket
        implements Packet {
    private float health;
    private int food;
    private float saturation;

    private ServerUpdateHealthPacket() {
    }

    public ServerUpdateHealthPacket(float health, int food, float saturation) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    public float getHealth() {
        return this.health;
    }

    public int getFood() {
        return this.food;
    }

    public float getSaturation() {
        return this.saturation;
    }

    public void read(NetIn in) throws IOException {
        this.health = in.readFloat();
        this.food = in.readShort();
        this.saturation = in.readFloat();
    }

    public void write(NetOut out) throws IOException {
        out.writeFloat(this.health);
        out.writeShort(this.food);
        out.writeFloat(this.saturation);
    }

    public boolean isPriority() {
        return false;
    }
}