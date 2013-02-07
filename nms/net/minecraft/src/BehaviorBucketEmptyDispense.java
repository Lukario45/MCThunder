package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorBucketEmptyDispense extends BehaviorDefaultDispenseItem
{
    private final BehaviorDefaultDispenseItem field_92073_c;

    /** Reference to the MinecraftServer object. */
    final MinecraftServer mcServer;

    public BehaviorBucketEmptyDispense(MinecraftServer par1)
    {
        this.mcServer = par1;
        this.field_92073_c = new BehaviorDefaultDispenseItem();
    }

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.getFront(par1IBlockSource.func_82620_h());
        World var4 = par1IBlockSource.getWorld();
        int var5 = par1IBlockSource.getXInt() + var3.getFrontOffsetX();
        int var6 = par1IBlockSource.getYInt();
        int var7 = par1IBlockSource.getZInt() + var3.getFrontOffsetZ();
        Material var8 = var4.getBlockMaterial(var5, var6, var7);
        int var9 = var4.getBlockMetadata(var5, var6, var7);
        Item var10;

        if (Material.water.equals(var8) && var9 == 0)
        {
            var10 = Item.bucketWater;
        }
        else
        {
            if (!Material.lava.equals(var8) || var9 != 0)
            {
                return super.dispenseStack(par1IBlockSource, par2ItemStack);
            }

            var10 = Item.bucketLava;
        }

        var4.setBlockWithNotify(var5, var6, var7, 0);

        if (--par2ItemStack.stackSize == 0)
        {
            par2ItemStack.itemID = var10.itemID;
            par2ItemStack.stackSize = 1;
        }
        else if (((TileEntityDispenser)par1IBlockSource.func_82619_j()).addItem(new ItemStack(var10)) < 0)
        {
            this.field_92073_c.dispense(par1IBlockSource, new ItemStack(var10));
        }

        return par2ItemStack;
    }
}
