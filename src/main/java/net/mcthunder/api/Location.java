package net.mcthunder.api;

import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.metadata.Position;

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
    private Vector v;

    public Location(World world, Position p) {
        this(world, p, 0, 0);
    }

    public Location(World world, Position p, float yaw, float pitch) {
        this(world, p.getX(), p.getY(), p.getZ(), yaw, pitch, new Vector(0, 0, 0));
    }

    public Location(World world, double x, double y, double z) {
        this(world, x, y, z, 0, 0, new Vector(0, 0, 0));
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        this(world, x, y, z, yaw, pitch, new Vector(0, 0, 0));
    }

    public Location(World world, Position p, Vector v) {
        this(world, p, 0, 0, v);
    }

    public Location(World world, Position p, float yaw, float pitch, Vector v) {
        this(world, p.getX(), p.getY(), p.getZ(), yaw, pitch, v);
    }

    public Location(World world, double x, double y, double z, Vector v) {
        this(world, x, y, z, 0, 0, v);
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch, Vector v) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.v = v;
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

    public void setVector(Vector v) {
        this.v = v;
    }

    public Vector getVector() {
        return this.v;
    }

    public String toString() {
        return this.world.getName() + " " + this.x + " " + this.y + " " + this.z + " " + this.yaw + " " + this.pitch;
    }

    public Position getPosition() {
        return new Position((int) getX(), (int) getY(), (int) getZ());
    }

    public boolean equals(Location l) {
        return this.x == l.getX() && this.y == l.getY() && this.z == l.getZ() && this.yaw == l.getYaw() && this.pitch == l.getPitch() && this.world == l.getWorld();
    }

    @Override
    public Location clone() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }
}