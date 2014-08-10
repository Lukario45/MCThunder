package net.mcthunder.message;

public class ClickEvent
        implements Cloneable {
    private ClickAction action;
    private String value;

    public ClickEvent(ClickAction action, String value) {
        this.action = action;
        this.value = value;
    }

    public ClickAction getAction() {
        return this.action;
    }

    public String getValue() {
        return this.value;
    }

    public ClickEvent clone() {
        return new ClickEvent(this.action, this.value);
    }
}