package net.mcthunder.apis;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * Based of of Alphabot/Lukabot code that was created by zack6849
 */
public class CommandRegistry {
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void register(Command command) {
        if (commands.containsKey(command.getName())) {
            return;
        }
        commands.put(command.getName(), command);
    }

    public static void registerCommands() {

    }


    public static void unregister(Command command) {
        commands.remove(command.getName());
    }

    public static Command getCommand(String name, String pkg) {
        if (commands.containsKey(name)) {
            return commands.get(name);
        }
        try {
            commands.put(name, (Command) Command.class.getClassLoader().loadClass(pkg + StringUtils.capitalize(name)).newInstance());
            return commands.get(name);
        } catch (Exception e) {

        }
        return null;
    }
}
