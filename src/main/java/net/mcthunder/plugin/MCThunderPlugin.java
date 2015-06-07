package net.mcthunder.plugin;

import java.util.List;

/**
 * Created by conno_000 on 6/6/2015.
 */
public abstract class MCThunderPlugin {
   private final String pluginName;
   private final String version;
   private final List<String> developers;
   private final String commandPath;

   public MCThunderPlugin(String pluginName, String version, List<String> developers, String commandPath){
      this.pluginName = pluginName;
      this.version = version;
      this.developers = developers;
      this.commandPath = commandPath;
   }

   public abstract boolean load();

   public String getPluginName(){
      return this.pluginName;
   }
   public String getVersion(){
      return this.version;
   }
   public List<String> getDevelopers(){
      return this.developers;
   }
   public String getCommandPath(){
      return this.commandPath;
   }


}
