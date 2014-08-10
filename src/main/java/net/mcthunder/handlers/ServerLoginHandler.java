package net.mcthunder.handlers;


import net.mcthunder.packet.essentials.Session;

public abstract interface ServerLoginHandler {
    public abstract void loggedIn(Session paramSession);
}