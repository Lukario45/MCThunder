package net.mcthunder.events;

import java.util.EventObject;

public class PlayerCommandEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PlayerCommandEvent(Object source) {
        super(source);
    }
}
