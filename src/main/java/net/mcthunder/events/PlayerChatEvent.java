package net.mcthunder.events;

import java.util.EventObject;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerChatEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public PlayerChatEvent(Object source) {
        super(source);
    }
}
