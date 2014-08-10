package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityRemoveEffectPacket
        implements Packet {
    private int entityId;
    private Effect effect;

    private ServerEntityRemoveEffectPacket() {
    }

    public ServerEntityRemoveEffectPacket(int entityId, Effect effect) {
        this.entityId = entityId;
        this.effect = effect;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.effect = Effect.values()[(in.readByte() - 1)];
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(this.effect.ordinal() + 1);
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