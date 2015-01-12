package net.mcthunder.api;

public class Decoration {
    private String id;
    private byte type;
    private double x, z, rotation;

    public Decoration(String id, byte type, double x, double z, double rotation) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.z = z;
        this.rotation = rotation;
    }

    public void setID(String newID) {
        this.id = newID;
    }

    public String getID() {
        return this.id;
    }

    public void setType(byte newType) {
        this.type = newType;
    }

    public byte getType() {
        return this.type;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public double getX() {
        return this.x;
    }

    public void setZ(double newZ) {
        this.z = newZ;
    }

    public double getZ() {
        return this.z;
    }

    public void setRotation(double newRotation) {
        this.rotation = newRotation;
    }

    public double getRotation() {
        return this.rotation;
    }
}