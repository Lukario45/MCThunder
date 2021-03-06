package net.mcthunder.events;

import java.util.EventObject;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerPlaceBlockEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PlayerPlaceBlockEvent(Object source) {
        super(source);
    }
}
