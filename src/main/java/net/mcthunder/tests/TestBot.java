package net.mcthunder.tests;

import net.mcthunder.api.Bot;
import net.mcthunder.api.LoggingLevel;

import java.util.UUID;

import static net.mcthunder.api.Utils.tellConsole;

public class TestBot extends Bot {
    //This can be removed at some point as well as this package. It will be used for testing things that don't have a home
    public TestBot(String name) {
        super(name);
    }

    @Override
    public void unload() {
        tellConsole(LoggingLevel.DEBUG, "TestBot(" + getName() + ") unloaded.");
    }

    @Override
    public void load() {
        tellConsole(LoggingLevel.DEBUG, "TestBot(" + getName() + ") loaded.");
    }
}
