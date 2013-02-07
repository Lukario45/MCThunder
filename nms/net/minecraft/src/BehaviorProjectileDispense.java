package net.minecraft.src;

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem
{
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseStack(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        World var3 = par1IBlockSource.getWorld();
        IPosition var4 = BlockDispenser.func_82525_a(par1IBlockSource);
        EnumFacing var5 = EnumFacing.getFront(par1IBlockSource.func_82620_h());
        IProjectile var6 = this.getProjectileEntity(var3, var4);
        var6.setThrowableHeading((double)var5.getFrontOffsetX(), 0.10000000149011612D, (double)var5.getFrontOffsetZ(), this.func_82500_b(), this.func_82498_a());
        var3.spawnEntityInWorld((Entity)var6);
        par2ItemStack.splitStack(1);
        return par2ItemStack;
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource par1IBlockSource)
    {
        par1IBlockSource.getWorld().playAuxSFX(1002, par1IBlockSource.getXInt(), par1IBlockSource.getYInt(), par1IBlockSource.getZInt(), 0);
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected abstract IProjectile getProjectileEntity(World var1, IPosition var2);

    protected float func_82498_a()
    {
        return 6.0F;
    }

    protected float func_82500_b()
    {
        return 1.1F;
    }
}
