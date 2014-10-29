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
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
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
        return (float) this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return (float) this.pitch;
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
}