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

    String name;

    LoggingLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
