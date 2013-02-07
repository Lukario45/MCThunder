package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class TileEntityCommandBlock extends TileEntity implements ICommandSender
{
    /** The command this block will execute when powered. */
    private String command = "";

    /**
     * Sets the command this block will execute when powered.
     */
    public void setCommand(String par1Str)
    {
        this.command = par1Str;
        this.onInventoryChanged();
    }

    /**
     * Execute the command, called when the command block is powered.
     */
    public void executeCommandOnPowered(World par1World)
    {
        if (!par1World.isRemote)
        {
            MinecraftServer var2 = MinecraftServer.getServer();

            if (var2 != null && var2.isCommandBlockEnabled())
            {
                ICommandManager var3 = var2.getCommandManager();
                var3.executeCommand(this, this.command);
            }
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return "@";
    }

    public void sendChatToPlayer(String par1Str) {}

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return par1 <= 2;
    }

    /**
     * Translates and formats the given string key with the given arguments.
     */
    public String translateString(String par1Str, Object ... par2ArrayOfObj)
    {
        return par1Str;
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setString("Command", this.command);
    }

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.command = par1NBTTagCompound.getString("Command");
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getCommandSenderPosition()
    {
        return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, var1);
    }
}
