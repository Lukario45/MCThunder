package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class CommandServerSaveOff extends CommandBase
{
    public String getCommandName()
    {
        return "save-off";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        MinecraftServer var3 = MinecraftServer.getServer();

        for (int var4 = 0; var4 < var3.worldServers.length; ++var4)
        {
            if (var3.worldServers[var4] != null)
            {
                WorldServer var5 = var3.worldServers[var4];
                var5.levelSaving = true;
            }
        }

        notifyAdmins(par1ICommandSender, "commands.save.disabled", new Object[0]);
    }
}
