package net.mcthunder.apis;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * Created by Kevin on 10/13/2014.
 */
public class CommandRegistry {
    public static HashMap<String, Command> commands = new HashMap<>();

    public static void register(Command command) {
        if (commands.containsKey(command.getName())) {
            return;
        }
        commands.put(command.getName(), command);
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
            e.printStackTrace();
        }
        return null;
    }
}
