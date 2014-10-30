package net.mcthunder.api;

import org.spacehq.mc.protocol.data.game.values.Face;

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
    DOWN("DOWN");

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

    public static Direction fromFace(Face side) {
        if (side.equals(Face.NORTH))
            return NORTH;
        if (side.equals(Face.EAST))
            return EAST;
        if (side.equals(Face.SOUTH))
            return SOUTH;
        if (side.equals(Face.WEST))
            return WEST;
        if (side.equals(Face.TOP))
            return UP;
        if (side.equals(Face.BOTTOM))
            return DOWN;
        return null;
    }

    private String name;

    private Direction(String name) {
        this.name = name;
    }
}