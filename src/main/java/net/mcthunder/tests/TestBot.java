package net.mcthunder.tests;

import net.mcthunder.api.Bot;
import net.mcthunder.api.LoggingLevel;

import static net.mcthunder.api.Utils.tellConsole;

public class TestBot extends Bot {
    //This can be removed at some point as well as this package. It will be used for testing things that don't have a home
    public TestBot(String name) {
        super(name);
    }

    @Override
    public void unload() {
        tellConsole(LoggingLevel.DEBUG, "TestBot(" + getName() + "&r) unloaded.");
    }

    @Override
    public void load() {
        tellConsole(LoggingLevel.DEBUG, "TestBot(" + getName() + "&r) loaded.");
    }

    @Override
    public void ai() {//Random test ai for testing entity movement for when I get around to making entity ais
        moveUp(0.1);
        moveForward(0.5);
        turnRight(5);
    }
}