package net.minecraft.src;

public class ItemMultiTextureTile extends ItemBlock
{
    private final Block theBlock;
    private final String[] field_82804_b;

    public ItemMultiTextureTile(int par1, Block par2Block, String[] par3ArrayOfStr)
    {
        super(par1);
        this.theBlock = par2Block;
        this.field_82804_b = par3ArrayOfStr;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int par1)
    {
        return par1;
    }

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        int var2 = par1ItemStack.getItemDamage();

        if (var2 < 0 || var2 >= this.field_82804_b.length)
        {
            var2 = 0;
        }

        return super.getItemName() + "." + this.field_82804_b[var2];
    }
}
