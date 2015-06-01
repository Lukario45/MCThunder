package net.mcthunder.commands

import net.mcthunder.MCThunder;
import net.mcthunder.api.Command;

public class Broadcast extends Command {
  public Broadcast(){
    super("broadcast", Arrays.asList("say"), "Broadcast a sever message", "/broadcast {message}", 5000, "core.broadcast");
  }
  
  @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        
        return true;
    }
}
