package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.world.block.BlockFace;

public enum Direction {
    NORTH("NORTH"),
    NORTH_EAST("NORTH_EAST"),
    EAST("EAST"),
    SOUTH_EAST("SOUTH_EAST"),
    SOUTH("SOUTH"),
    SOUTH_WEST("SOUTH_WEST"),
    WEST("WEST"),
    NORTH_WEST("NORTH_WEST"),
    UP("UP"),
    DOWN("DOWN"),
    SPECIAL("SPECIAL");

    public static Direction fromString(String name) {
        if(name.equalsIgnoreCase("north"))
            return NORTH;
        if(name.equalsIgnoreCase("north_east"))
            return NORTH_EAST;
        if(name.equalsIgnoreCase("east"))
            return EAST;
        if(name.equalsIgnoreCase("south_east"))
            return SOUTH_EAST;
        if(name.equalsIgnoreCase("south"))
            return SOUTH;
        if(name.equalsIgnoreCase("south_west"))
            return SOUTH_WEST;
        if(name.equalsIgnoreCase("west"))
            return WEST;
        if(name.equalsIgnoreCase("north_west"))
            return NORTH_WEST;
        if(name.equalsIgnoreCase("up"))
            return UP;
        return DOWN;
    }

    public static Direction fromFace(BlockFace side) {
        if (side.equals(BlockFace.NORTH))
            return NORTH;
        if (side.equals(BlockFace.EAST))
            return EAST;
        if (side.equals(BlockFace.SOUTH))
            return SOUTH;
        if (side.equals(BlockFace.WEST))
            return WEST;
        if (side.equals(BlockFace.UP))
            return UP;
        if (side.equals(BlockFace.DOWN))
            return DOWN;
        if (side.equals(BlockFace.SPECIAL))
            return SPECIAL;
        return null;
    }

    private String name;

    private Direction(String name) {
        this.name = name;
    }
}