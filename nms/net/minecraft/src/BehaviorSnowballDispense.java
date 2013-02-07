package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorSnowballDispense extends BehaviorProjectileDispense
{
    /** Instance of MinecraftServer. */
    final MinecraftServer theServer;

    public BehaviorSnowballDispense(MinecraftServer par1)
    {
        this.theServer = par1;
    }

    /**
     * Return the projectile entity spawned by this dispense behavior.
     */
    protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        return new EntitySnowball(par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
    }
}
