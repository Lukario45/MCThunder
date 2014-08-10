package net.mcthunder.packet.ingame.server.entity.player;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerSetExperiencePacket
        implements Packet {
    private float experience;
    private int level;
    private int totalExperience;

    private ServerSetExperiencePacket() {
    }

    public ServerSetExperiencePacket(float experience, int level, int totalExperience) {
        this.experience = experience;
        this.level = level;
        this.totalExperience = totalExperience;
    }

    public float getSlot() {
        return this.experience;
    }

    public int getLevel() {
        return this.level;
    }

    public int getTotalExperience() {
        return this.totalExperience;
    }

    public void read(NetIn in) throws IOException {
        this.experience = in.readFloat();
        this.level = in.readShort();
        this.totalExperience = in.readShort();
    }

    public void write(NetOut out) throws IOException {
        out.writeFloat(this.experience);
        out.writeShort(this.level);
        out.writeShort(this.totalExperience);
    }

    public boolean isPriority() {
        return false;
    }
}