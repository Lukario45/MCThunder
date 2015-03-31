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

    public static Command getCommand(String name) {
        if (commands.containsKey(name.toLowerCase()))
            return commands.get(name.toLowerCase());
        for (Command c : commands.values())
            if (c.getAliases().contains(name))
                return c;
        return null;
    }

    public static Command loadCommand(String name, String pkg) {
        try {
            //Add a better way to replace to get plugin name or things
            String header = commands.containsKey(name.toLowerCase()) ? pkg.replace("net.mcthunder.", "").replace(".commands.", "").replace("commands.", "") + ":" : "";
            commands.put((header + name.toLowerCase()).trim(), (Command) Command.class.getClassLoader().loadClass(pkg + StringUtils.capitalize(name)).newInstance());
            return commands.get((header + name.toLowerCase()).trim());
        } catch (Exception ignored) { }
        return null;
    }

    /*public static Command getCommand(Node node) {//TODO: Create a node object
        if (node == null)
            return null;
        for (Command c : commands.values())
            if (c.getPermissionNode().equals(node))
                return c;
        return null;
    }*/

    public static HashMap<String,Command> getCommands() {
        return commands;
    }
}