package net.mcthunder.packet.ingame.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerRespawnPacket
        implements Packet {
    private int dimension;
    private Difficulty difficulty;
    private GameMode gamemode;
    private WorldType worldType;

    private ServerRespawnPacket() {
    }

    public ServerRespawnPacket(int dimension, Difficulty difficulty, GameMode gamemode, WorldType worldType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gamemode = gamemode;
        this.worldType = worldType;
    }

    private static String typeToName(WorldType type) throws IOException {
        if (type == WorldType.DEFAULT)
            return "default";
        if (type == WorldType.FLAT)
            return "flat";
        if (type == WorldType.LARGE_BIOMES)
            return "largeBiomes";
        if (type == WorldType.AMPLIFIED)
            return "amplified";
        if (type == WorldType.DEFAULT_1_1) {
            return "default_1_1";
        }
        throw new IOException("Unmapped world type: " + type);
    }

    private static WorldType nameToType(String name) throws IOException {
        if (name.equalsIgnoreCase("default"))
            return WorldType.DEFAULT;
        if (name.equalsIgnoreCase("flat"))
            return WorldType.FLAT;
        if (name.equalsIgnoreCase("largeBiomes"))
            return WorldType.LARGE_BIOMES;
        if (name.equalsIgnoreCase("amplified"))
            return WorldType.AMPLIFIED;
        if (name.equalsIgnoreCase("default_1_1")) {
            return WorldType.DEFAULT_1_1;
        }
        throw new IOException("Unknown world type: " + name);
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

    public void read(NetIn in) throws IOException {
        this.dimension = in.readInt();
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
        this.gamemode = GameMode.values()[in.readUnsignedByte()];
        this.worldType = nameToType(in.readString());
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.dimension);
        out.writeByte(this.difficulty.ordinal());
        out.writeByte(this.gamemode.ordinal());
        out.writeString(typeToName(this.worldType));
    }

    public boolean isPriority() {
        return false;
    }

    public static enum WorldType {
        DEFAULT,
        FLAT,
        LARGE_BIOMES,
        AMPLIFIED,
        DEFAULT_1_1;
    }

    public static enum Difficulty {
        PEACEFUL,
        EASY,
        NORMAL,
        HARD;
    }

    public static enum GameMode {
        SURVIVAL,
        CREATIVE,
        ADVENTURE;
    }
}