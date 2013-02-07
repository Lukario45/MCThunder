package net.minecraft.src;

public class TileEntityRecordPlayer extends TileEntity
{
    /** ID of record which is in Jukebox */
    public ItemStack record;

    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("RecordItem"))
        {
            this.record = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("RecordItem"));
        }
        else
        {
            this.record = new ItemStack(par1NBTTagCompound.getInteger("Record"), 1, 0);
        }
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        if (this.record != null)
        {
            par1NBTTagCompound.setCompoundTag("RecordItem", this.record.writeToNBT(new NBTTagCompound()));
            par1NBTTagCompound.setInteger("Record", this.record.itemID);
        }
    }
}
