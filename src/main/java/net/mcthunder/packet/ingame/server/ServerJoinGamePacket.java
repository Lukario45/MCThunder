package net.mcthunder.packet.ingame.server;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerJoinGamePacket
        implements Packet {
    private int entityId;
    private boolean hardcore;
    private GameMode gamemode;
    private int dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;

    private ServerJoinGamePacket() {
    }

    public ServerJoinGamePacket(int entityId, boolean hardcore, GameMode gamemode, int dimension, Difficulty difficulty, int maxPlayers, WorldType worldType) {
        this.entityId = entityId;
        this.hardcore = hardcore;
        this.gamemode = gamemode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
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

    public int getEntityId() {
        return this.entityId;
    }

    public boolean getHardcore() {
        return this.hardcore;
    }

    public GameMode getGameMode() {
        return this.gamemode;
    }

    public int getDimension() {
        return this.dimension;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public WorldType getWorldType() {
        return this.worldType;
    }

    public void read(NetIn in) throws IOException {
        this.entityId = in.readInt();
        int gamemode = in.readUnsignedByte();
        this.hardcore = ((gamemode & 0x8) == 8);
        gamemode &= -9;
        this.gamemode = GameMode.values()[gamemode];
        this.dimension = in.readByte();
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
        this.maxPlayers = in.readUnsignedByte();
        this.worldType = nameToType(in.readString());
    }

    public void write(NetOut out) throws IOException {
        out.writeInt(this.entityId);
        int gamemode = this.gamemode.ordinal();
        if (this.hardcore) {
            gamemode |= 8;
        }

        out.writeByte(gamemode);
        out.writeByte(this.dimension);
        out.writeByte(this.difficulty.ordinal());
        out.writeByte(this.maxPlayers);
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