package net.minecraft.src;

public class RConConsoleSource implements ICommandSender
{
    /** Single instance of RConConsoleSource */
    public static final RConConsoleSource instance = new RConConsoleSource();

    /** RCon string buffer for log */
    private StringBuffer buffer = new StringBuffer();

    /**
     * Clears the RCon log
     */
    public void resetLog()
    {
        this.buffer.setLength(0);
    }

    /**
     * Gets the contents of the RCon log
     */
    public String getLogContents()
    {
        return this.buffer.toString();
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return "Rcon";
    }

    public void sendChatToPlayer(String par1Str)
    {
        this.buffer.append(par1Str);
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return true;
    }

    /**
     * Translates and formats the given string key with the given arguments.
     */
    public String translateString(String par1Str, Object ... par2ArrayOfObj)
    {
        return StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj);
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getCommandSenderPosition()
    {
        return new ChunkCoordinates(0, 0, 0);
    }
}
