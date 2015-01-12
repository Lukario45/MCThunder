package net.mcthunder.api;

public class Modifier {
    private String attributeName, name;
    private double amount;
    private int operation;
    private long uuidMost, uuidLeast;

    public Modifier(String attributeName, String name, double amount, int operation, long uuidMost, long uuidLeast) {
        this.attributeName = attributeName;
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.uuidMost = uuidMost;
        this.uuidLeast = uuidLeast;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return this.operation;
    }

    public void setUUIDMost(long uuidMost) {
        this.uuidMost = uuidMost;
    }

    public long getUUIDMost() {
        return this.uuidMost;
    }

    public void setUUIDLeast(long uuidLeast) {
        this.uuidLeast = uuidLeast;
    }

    public long getUUIDLeast() {
        return this.uuidLeast;
    }
}