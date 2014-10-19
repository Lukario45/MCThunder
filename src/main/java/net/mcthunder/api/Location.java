package net.mcthunder.api;

/**
 * Created by zack6849 on 10/17/14.
 */
public class Location {
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public Location(double x, double y, double z) {
        new Location(x, y, z, 0, 0);
    }

    public Location(double x, double y, double z, double yaw, double pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
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
}
