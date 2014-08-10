package net.mcthunder.packet.ingame.server.entity;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerEntityStatusPacket
        implements Packet {
    protected int entityId;
    protected Status status;

    private ServerEntityStatusPacket() {
    }

    public ServerEntityStatusPacket(int entityId, Status status) {
        this.entityId = entityId;
        this.status = status;
    }

    private static byte statusToValue(Status status) throws IOException {
        switch (status.ordinal()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 11;
            case 11:
                return 12;
            case 12:
                return 13;
            case 13:
                return 14;
            case 14:
                return 15;
            case 15:
                return 16;
            case 16:
                return 17;
            case 17:
                return 18;
        }
        throw new IOException("Unmapped entity status: " + status);
    }

    private static Status valueToStatus(byte value) throws IOException {
        switch (value) {
            case 1:
                return Status.HURT_OR_MINECART_SPAWNER_DELAY_RESET;
            case 2:
                return Status.LIVING_HURT;
            case 3:
                return Status.DEAD;
            case 4:
                return Status.IRON_GOLEM_THROW;
            case 6:
                return Status.TAMING;
            case 7:
                return Status.TAMED;
            case 8:
                return Status.WOLF_SHAKING;
            case 9:
                return Status.FINISHED_EATING;
            case 10:
                return Status.SHEEP_GRAZING_OR_TNT_CART_EXPLODING;
            case 11:
                return Status.IRON_GOLEM_ROSE;
            case 12:
                return Status.VILLAGER_HEARTS;
            case 13:
                return Status.VILLAGER_ANGRY;
            case 14:
                return Status.VILLAGER_HAPPY;
            case 15:
                return Status.WITCH_MAGIC_PARTICLES;
            case 16:
                return Status.ZOMBIE_VILLAGER_SHAKING;
            case 17:
                return Status.FIREWORK_EXPLODING;
            case 18:
                return Status.ANIMAL_HEARTS;
            case 5:
        }
        throw new IOException("Unknown entity status value: " + value);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public Status getStatus() {
        return this.status;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        this.status = valueToStatus(in.readByte());
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        out.writeByte(statusToValue(this.status));
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Status {
        HURT_OR_MINECART_SPAWNER_DELAY_RESET,
        LIVING_HURT,
        DEAD,
        IRON_GOLEM_THROW,
        TAMING,
        TAMED,
        WOLF_SHAKING,
        FINISHED_EATING,
        SHEEP_GRAZING_OR_TNT_CART_EXPLODING,
        IRON_GOLEM_ROSE,
        VILLAGER_HEARTS,
        VILLAGER_ANGRY,
        VILLAGER_HAPPY,
        WITCH_MAGIC_PARTICLES,
        ZOMBIE_VILLAGER_SHAKING,
        FIREWORK_EXPLODING,
        ANIMAL_HEARTS;
    }
}