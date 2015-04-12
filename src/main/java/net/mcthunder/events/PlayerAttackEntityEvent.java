package net.mcthunder.events;

import java.util.EventObject;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerAttackEntityEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PlayerAttackEntityEvent(Object source) {
        super(source);
    }
}