package net.mcthunder.api;

import net.mcthunder.world.World;

/**
 * Created by zack6849 on 10/17/14.
 */
public class Location {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private World world;

    public Location(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /*public Location getRelative(double xz, double xy, double distance) {//Will finish this at some point but not now
        while (xz >= 360)
            xz -= 360;
        while (xz < 360)
            xz += 360;
        if (xz > 180)
            xz -= 360;
        if (xz <= -180)
            xz += 360;
        while (xy >= 360)
            xy -= 360;
        while (xy < 360)
            xy += 360;
        if (xy > 180)
            xy -= 360;
        if (xy <= -180)
            xy += 360;
        double x = 0;
        double y = 0;
        double z = 0;
        //xz 0 --> south
        //-45 < xz < 45
        //xz 90 --> west
        //
        //xz 180 --> north
        //
        //xz -90 --> east
        //
        return getRelative(x, y, z, distance);
    }*/

    public Location getRelative(double x, double y, double z, double distance) {
        return new Location(this.world, this.x + x*distance, this.y + y*distance, this.z + z*distance, this.yaw, this.pitch);
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public World getWorld() {
        return this.world;
    }

    public void setWorld(World w) {
        this.world = w;
    }

    public String toString() {
        return this.world.getName() + " " + this.x + " " + this.y + " " + this.z + " " + this.yaw + " " + this.pitch;
    }

    @Override
    public Location clone() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
}