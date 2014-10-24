package net.mcthunder.api;

import net.mcthunder.world.World;

/**
 * Created by zack6849 on 10/17/14.
 */
public class Location {
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;
    private World world;

    public Location(World world, double x, double y, double z) {
        new Location(world, x, y, z, 0, 0);
    }

    public Location(World world, double x, double y, double z, double yaw, double pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return (float) this.yaw;
    }


    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return (float) pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public World getWorld() {
        return this.world;
    }
}
