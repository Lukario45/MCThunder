package net.minecraft.src;

import java.util.List;
import net.minecraft.server.MinecraftServer;

public class CommandServerBanlist extends CommandBase
{
    public String getCommandName()
    {
        return "banlist";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return (MinecraftServer.getServer().getConfigurationManager().getBannedIPs().isListActive() || MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().isListActive()) && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.banlist.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1 && par2ArrayOfStr[0].equalsIgnoreCase("ips"))
        {
            par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.banlist.ips", new Object[] {Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getBannedList().size())}));
            par1ICommandSender.sendChatToPlayer(joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedIPs().getBannedList().keySet().toArray()));
        }
        else
        {
            par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.banlist.players", new Object[] {Integer.valueOf(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getBannedList().size())}));
            par1ICommandSender.sendChatToPlayer(joinNiceString(MinecraftServer.getServer().getConfigurationManager().getBannedPlayers().getBannedList().keySet().toArray()));
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[] {"players", "ips"}): null;
    }
}
