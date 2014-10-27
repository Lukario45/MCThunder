package net.mcthunder.api;

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

    public Direction fromString(String name) {
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

    private String name;

    private Direction(String name) {
        this.name = name;
    }
}