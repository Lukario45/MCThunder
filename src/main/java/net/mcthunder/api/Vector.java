package net.mcthunder.api;

public class Vector {
    private double dX, dY, dZ;

    public Vector(double dX, double dY, double dZ) {
        this.dX = dX;
        this.dY = dY;
        this.dZ = dZ;
    }

    public double getdX() {
        return this.dX;
    }

    public double getdY() {
        return this.dY;
    }

    public double getdZ() {
        return this.dZ;
    }
}