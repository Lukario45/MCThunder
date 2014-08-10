package net.mcthunder.message;

public class HoverEvent
        implements Cloneable {
    private HoverAction action;
    private Message value;

    public HoverEvent(HoverAction action, Message value) {
        this.action = action;
        this.value = value;
    }

    public HoverAction getAction() {
        return this.action;
    }

    public Message getValue() {
        return this.value;
    }

    public HoverEvent clone() {
        return new HoverEvent(this.action, this.value.clone());
    }
}