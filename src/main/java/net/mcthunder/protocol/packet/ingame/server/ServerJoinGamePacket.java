package net.mcthunder.protocol.packet.ingame.server;

import net.mcthunder.packetlib.io.NetInput;
import net.mcthunder.packetlib.io.NetOutput;
import net.mcthunder.packetlib.packet.Packet;

import java.io.IOException;

public class ServerJoinGamePacket implements Packet {

    private int entityId;
    private boolean hardcore;
    private GameMode gamemode;
    private int dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;

    @SuppressWarnings("unused")
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

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readInt();
        int gamemode = in.readUnsignedByte();
        this.hardcore = (gamemode & 8) == 8;
        gamemode = gamemode & -9;
        this.gamemode = GameMode.values()[gamemode];
        this.dimension = in.readByte();
        this.difficulty = Difficulty.values()[in.readUnsignedByte()];
        this.maxPlayers = in.readUnsignedByte();
        this.worldType = nameToType(in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
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
