package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityEffectPacket
        implements Packet {
    private int entityId;
    private Effect effect;
    private int amplifier;
    private int duration;

    private ServerEntityEffectPacket() {
    }

    public ServerEntityEffectPacket(int entityId, Effect effect, int amplifier, int duration) {
        this.entityId = entityId;
        this.effect = effect;
        this.amplifier = amplifier;
        this.duration = duration;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public int getDuration() {
        return this.duration;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.effect = Effect.values()[(in.readByte() - 1)];
        this.amplifier = in.readByte();
        this.duration = in.readShort();
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(this.effect.ordinal() + 1);
        out.writeByte(this.amplifier);
        out.writeShort(this.duration);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Effect {
        SPEED,
        SLOWNESS,
        DIG_SPEED,
        DIG_SLOWNESS,
        DAMAGE_BOOST,
        HEAL,
        DAMAGE,
        ENHANCED_JUMP,
        CONFUSION,
        REGENERATION,
        RESISTANCE,
        FIRE_RESISTANCE,
        WATER_BREATHING,
        INVISIBILITY,
        BLINDNESS,
        NIGHT_VISION,
        HUNGER,
        WEAKNESS,
        POISON,
        WITHER_EFFECT,
        HEALTH_BOOST,
        ABSORPTION,
        SATURATION;
    }
}