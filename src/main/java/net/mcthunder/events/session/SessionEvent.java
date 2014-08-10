package net.mcthunder.events.session;

import net.mcthunder.listeners.SessionListener;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface SessionEvent {
    public abstract void call(SessionListener paramSessionListener);
}
