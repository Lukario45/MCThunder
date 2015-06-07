package net.mcthunder.plugin;

import java.util.List;

/**
 * Created by conno_000 on 6/6/2015.
 */
public abstract class MCThunderPlugin {
    private final String pluginName;
    private final String version;
    private final List<String> developers, loadAfter;
    private final String commandPath;

    public MCThunderPlugin(String pluginName, String version, List<String> developers, String commandPath){
        this(pluginName, version, developers, commandPath, null);
    }

    public MCThunderPlugin(String pluginName, String version, List<String> developers, String commandPath, List<String> loadAfter){
        this.pluginName = pluginName;
        this.version = version;
        this.developers = developers;
        this.commandPath = commandPath;
        this.loadAfter = loadAfter;
    }

    public abstract boolean load();

    public abstract boolean unload();//Should this be a void or do we tell them if it failed to unload properly since server shuts down anyways

    public final String getPluginName() {
       return this.pluginName;
    }

    public final String getVersion() {
       return this.version;
    }

    public final List<String> getDevelopers() {
       return this.developers;
    }

    public final String getCommandPath() {
       return this.commandPath;
    }

    public final List<String> getLoadAfter() {
        return this.loadAfter;
    }
}