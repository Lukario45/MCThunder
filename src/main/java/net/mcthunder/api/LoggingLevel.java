package net.mcthunder.api;

/**
 * Created by zack6849 on 10/17/14.
 */
public enum LoggingLevel {
    DEBUG("DEBUG"),
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    SEVERE("SEVERE"),
    CHAT("CHAT"),
    COMMAND("COMMAND");

    private String name;

    private LoggingLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static LoggingLevel fromString(String name) {
        if (name.equalsIgnoreCase("info"))
            return INFO;
        if (name.equalsIgnoreCase("warning"))
            return WARNING;
        if (name.equalsIgnoreCase("error"))
            return ERROR;
        if (name.equalsIgnoreCase("severe"))
            return SEVERE;
        if (name.equalsIgnoreCase("chat"))
            return CHAT;
        if (name.equalsIgnoreCase("command"))
            return COMMAND;
        return DEBUG;
    }
}