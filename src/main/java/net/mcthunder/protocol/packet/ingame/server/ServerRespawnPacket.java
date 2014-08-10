package net.mcthunder.protocol.packet.ingame.server;

import org.spacehq.packetlib.io.NetInput;
import org.spacehq.packetlib.io.NetOutput;
import org.spacehq.packetlib.packet.Packet;

import java.io.IOException;

public class ServerRespawnPacket implements Packet {

    private int dimension;
    private Difficulty difficulty;
    private GameMode gamemode;
    private WorldType worldType;

    @SuppressWarnings("unused")
    private ServerRespawnPacket() {
    }

    public ServerRespawnPacket(int dimension, Difficulty difficulty, GameMode gamemode, WorldType worldType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gamemode = gamemode;
        this.worldType = worldType;
    }

    private static String typeToName(WorldType type) throws IOException {
        if (type == WorldType.DEFAULT) {
            return "default";
        } else if (type == WorldType.FLAT) {
            return "flat";
        } else if (type == WorldType.LARGE_BIOMES) {
            return "largeBiomes";
        } else if (type == WorldType.AMPLIFIED) {
            return "amplified";
        } else if (type == WorldType.DEFAULT_1_1) {
            return "default_1_1";
        } else {
            throw new IOException("Unmapped world type: " + type);
        }
    }

    private static WorldType nameToType(String name) throws IOException {
        if (name.equalsIgnoreCase("default")) {
            return WorldType.DEFAULT;
        } else if (name.equalsIgnoreCase("flat")) {
            return WorldType.FLAT;
        } else if (name.equalsIgnoreCase("largeBiomes")) {
            return WorldType.LARGE_BIOMES;
        } else if (name.equalsIgnoreCase("amplified")) {
            return WorldType.AMPLIFIED;
        } else if (name.equalsIgnoreCase("default_1_1")) {
            return WorldType.DEFAULT_1_1;
        } else {
            throw new IOException("Unknown world type: " + name);
        }
    }

    public int getDimension() {
        return this.dimension;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.dimension = in.readInt();
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
        this.gamemode = GameMode.values()[in.readUnsignedByte()];
        this.worldType = nameToType(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeInt(this.dimension);
        out.writeByte(this.difficulty.ordinal());
        out.writeByte(this.gamemode.ordinal());
        out.writeString(typeToName(this.worldType));
    }

    @Override
    public boolean isPriority() {
        return false;
    }

    public static enum GameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE;
    }

    public static enum Difficulty {
        PEACEFUL,
        EASY,
        NORMAL,
        HARD;
    }

    public static enum WorldType {
        DEFAULT,
        FLAT,
        LARGE_BIOMES,
        AMPLIFIED,
        DEFAULT_1_1;
    }

}
