package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerList extends CommandBase
{
    public String getCommandName()
    {
        return "list";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        par1ICommandSender.sendChatToPlayer(par1ICommandSender.translateString("commands.players.list", new Object[] {Integer.valueOf(MinecraftServer.getServer().getCurrentPlayerCount()), Integer.valueOf(MinecraftServer.getServer().getMaxPlayers())}));
        par1ICommandSender.sendChatToPlayer(MinecraftServer.getServer().getConfigurationManager().getPlayerListAsString());
    }
}
