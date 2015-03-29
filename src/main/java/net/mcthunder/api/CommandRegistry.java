package net.mcthunder.api;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * Based of of Alphabot/Lukabot code that was created by zack6849
 */
public class CommandRegistry {
    private static HashMap<String, Command> commands = new HashMap<>();

    public static void register(Command command) {
        if (command != null && !commands.containsKey(command.getName().toLowerCase()))
            commands.put(command.getName().toLowerCase(), command);
    }

    public static void registerCommands() {

    }

    public static void unregister(Command command) {
        commands.remove(command.getName());
    }

    public static Command getCommand(String name, String pkg) {
        if (commands.containsKey(name.toLowerCase()))
            return commands.get(name.toLowerCase());
        try {
            //Add a better way to replace to get plugin name or things
            String header = commands.containsKey(name.toLowerCase()) ? pkg.replaceFirst("net.mcthunder.", "").replace("commands", "") : "";
            commands.put(header + name.toLowerCase(), (Command) Command.class.getClassLoader().loadClass(pkg + StringUtils.capitalize(name)).newInstance());
            return commands.get(name.toLowerCase());
        } catch (Exception ignored) { }
        for (Command c : commands.values())
            if (c.getAliases().contains(name))
                return c;
        return null;
    }

    public static Command getCommand(String node) {
        for (Command c : commands.values())
            if (c.getPermissionNode().equalsIgnoreCase(node))
                return c;
        return null;
    }

    public static HashMap<String,Command> getCommands() {
        return commands;
    }
}