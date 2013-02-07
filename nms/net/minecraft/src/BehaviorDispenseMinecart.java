package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorDispenseMinecart extends BehaviorDefaultDispenseItem
{
    /** Reference to the BehaviorDefaultDispenseItem object. */
    private final BehaviorDefaultDispenseItem defaultItemDispenseBehavior;

    final MinecraftServer mcServer;

    public BehaviorDispenseMinecart(MinecraftServer par1MinecraftServer)
    {
        this.mcServer = par1MinecraftServer;
        this.defaultItemDispenseBehavior = new BehaviorDefaultDispenseItem();
    }

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        EnumFacing var3 = EnumFacing.getFront(par1IBlockSource.func_82620_h());
        World var4 = par1IBlockSource.getWorld();
        double var5 = par1IBlockSource.getX() + (double)((float)var3.getFrontOffsetX() * 1.125F);
        double var7 = par1IBlockSource.getY();
        double var9 = par1IBlockSource.getZ() + (double)((float)var3.getFrontOffsetZ() * 1.125F);
        int var11 = par1IBlockSource.getXInt() + var3.getFrontOffsetX();
        int var12 = par1IBlockSource.getYInt();
        int var13 = par1IBlockSource.getZInt() + var3.getFrontOffsetZ();
        int var14 = var4.getBlockId(var11, var12, var13);
        double var15;

        if (BlockRail.isRailBlock(var14))
        {
            var15 = 0.0D;
        }
        else
        {
            if (var14 != 0 || !BlockRail.isRailBlock(var4.getBlockId(var11, var12 - 1, var13)))
            {
                return this.defaultItemDispenseBehavior.dispense(par1IBlockSource, par2ItemStack);
            }

            var15 = -1.0D;
        }

        EntityMinecart var17 = new EntityMinecart(var4, var5, var7 + var15, var9, ((ItemMinecart)par2ItemStack.getItem()).minecartType);
        var4.spawnEntityInWorld(var17);
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.getWorld().playAuxSFX(1000, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
    }
}
