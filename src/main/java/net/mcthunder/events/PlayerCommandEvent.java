public class PlayerCommandEvent extends EventObject {
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
