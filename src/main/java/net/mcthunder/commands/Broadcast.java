package net.mcthunder.commands;

import net.mcthunder.api.Command;
import net.mcthunder.entity.Player;

import java.util.Arrays;

public class Broadcast extends Command {
  public Broadcast(){
    super("broadcast", Arrays.asList("say"), "Broadcast a server message", "/broadcast {message}", 5000, "core.broadcast");
  }
  
  @Override
    public boolean execute(Player player, String[] args) {
        if (args.length == 0)
            return false;
        
        return true;
    }
}