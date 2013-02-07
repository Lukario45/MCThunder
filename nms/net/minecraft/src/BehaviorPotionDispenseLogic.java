package net.minecraft.src;

class BehaviorPotionDispenseLogic extends BehaviorProjectileDispense
{
    final ItemStack potionStackToDispense;

    /** Reference to the BehaviorPotionDispense object. */
    final BehaviorPotionDispense potionDispenseBehavior;

    BehaviorPotionDispenseLogic(BehaviorPotionDispense par1, ItemStack par2)
    {
        this.potionDispenseBehavior = par1;
        this.potionStackToDispense = par2;
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        return new EntityPotion(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ(), this.potionStackToDispense.copy());
    }

    protected float func_82498_a()
    {
        return super.func_82498_a() * 0.5F;
    }

    protected float func_82500_b()
    {
        return super.func_82500_b() * 1.25F;
    }
}
