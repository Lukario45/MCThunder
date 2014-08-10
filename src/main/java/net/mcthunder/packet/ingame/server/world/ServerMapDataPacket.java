package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerMapDataPacket
        implements Packet {
    private int mapId;
    private Type type;
    private MapData data;

    private ServerMapDataPacket() {
    }

    public ServerMapDataPacket(int mapId, Type type, MapData data) {
        this.mapId = mapId;
        this.type = type;
        this.data = data;
    }

    public int getMapId() {
        return this.mapId;
    }

    public Type getType() {
        return this.type;
    }

    public MapData getData() {
        return this.data;
    }

    public void read(NetIn in) throws IOException {
        this.mapId = in.readVarInt();
        byte[] data = in.readBytes(in.readShort());
        this.type = Type.values()[data[0]];
        switch (this.type.ordinal()) {
            case 1:
                int x = data[1] & 0xFF;
                int y = data[2] & 0xFF;
                int height = data.length - 3;
                byte[] colors = new byte[height];
                for (int index = 0; index < height; index++) {
                    colors[index] = data[(index + 3)];
                }

                this.data = new MapColumnUpdate(x, y, height, colors);
                break;
            case 2:
                List players = new ArrayList();
                for (int index = 0; index < (data.length - 1) / 3; index++) {
                    int sizeRot = data[(index * 3 + 1)] & 0xFF;
                    int iconSize = sizeRot >> 4 & 0xFF;
                    int iconRotation = sizeRot & 0xF & 0xFF;
                    int centerX = data[(index * 3 + 2)] & 0xFF;
                    int centerY = data[(index * 3 + 3)] & 0xFF;
                    players.add(new MapPlayer(iconSize, iconRotation, centerX, centerY));
                }

                this.data = new MapPlayers(players);
                break;
            case 3:
                this.data = new MapScale(data[1] & 0xFF);
        }
    }

    public void write(NetOut out)
            throws IOException {
        out.writeVarInt(this.mapId);
        byte[] data = null;
        switch (this.type.ordinal()) {
            case 1:
                MapColumnUpdate column = (MapColumnUpdate) this.data;
                data = new byte[column.getHeight() + 3];
                data[0] = 0;
                data[1] = (byte) column.getX();
                data[2] = (byte) column.getY();
                for (int index = 3; index < data.length; index++) {
                    data[index] = column.getColors()[(index - 3)];
                }

                break;
            case 2:
                MapPlayers players = (MapPlayers) this.data;
                data = new byte[players.getPlayers().size() * 3 + 1];
                data[0] = 1;
                for (int index = 0; index < players.getPlayers().size(); index++) {
                    MapPlayer player = (MapPlayer) players.getPlayers().get(index);
                    data[(index * 3 + 1)] = (byte) ((byte) player.getIconSize() << 4 | (byte) player.getIconRotation() & 0xF);
                    data[(index * 3 + 2)] = (byte) player.getCenterX();
                    data[(index * 3 + 3)] = (byte) player.getCenterZ();
                }

                break;
            case 3:
                MapScale scale = (MapScale) this.data;
                data = new byte[2];
                data[0] = 2;
                data[1] = (byte) scale.getScale();
        }

        out.writeShort(data.length);
        out.writeBytes(data);
    }

    public boolean isPriority() {
        return false;
    }

    public static enum Type {
        IMAGE,
        PLAYERS,
        SCALE;
    }

    public static abstract interface MapData {
    }

    public static class MapScale
            implements ServerMapDataPacket.MapData {
        private int scale;

        public MapScale(int scale) {
            this.scale = scale;
        }

        public int getScale() {
            return this.scale;
        }
    }

    public static class MapPlayer {
        private int iconSize;
        private int iconRotation;
        private int centerX;
        private int centerZ;

        public MapPlayer(int iconSize, int iconRotation, int centerX, int centerZ) {
            this.iconSize = iconSize;
            this.iconRotation = iconRotation;
            this.centerX = centerX;
            this.centerZ = centerZ;
        }

        public int getIconSize() {
            return this.iconSize;
        }

        public int getIconRotation() {
            return this.iconRotation;
        }

        public int getCenterX() {
            return this.centerX;
        }

        public int getCenterZ() {
            return this.centerZ;
        }
    }

    public static class MapPlayers
            implements ServerMapDataPacket.MapData {
        private List<ServerMapDataPacket.MapPlayer> players = new ArrayList();

        public MapPlayers(List<ServerMapDataPacket.MapPlayer> players) {
            this.players = players;
        }

        public List<ServerMapDataPacket.MapPlayer> getPlayers() {
            return new ArrayList(this.players);
        }
    }

    public static class MapColumnUpdate
            implements ServerMapDataPacket.MapData {
        private int x;
        private int y;
        private int height;
        private byte[] colors;

        public MapColumnUpdate(int x, int y, int height, byte[] colors) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.colors = colors;
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public int getHeight() {
            return this.height;
        }

        public byte[] getColors() {
            return this.colors;
        }
    }
}