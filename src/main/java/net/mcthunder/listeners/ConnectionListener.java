package net.mcthunder.listeners;

/**
 * Created by Kevin on 8/9/2014.
 */
public abstract interface ConnectionListener {
    public abstract String getHost();

    public abstract int getPort();

    public abstract boolean isListening();

    public abstract void close();
}