package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class AABBPool
{
    /**
     * Maximum number of times the pool can be "cleaned" before the list is shrunk
     */
    private final int maxNumCleans;

    /**
     * Number of Pool entries to remove when cleanPool is called maxNumCleans times.
     */
    private final int numEntriesToRemove;

    /** List of AABB stored in this Pool */
    private final List listAABB = new ArrayList();

    /** Next index to use when adding a Pool Entry. */
    private int nextPoolIndex = 0;

    /**
     * Largest index reached by this Pool (can be reset to 0 upon calling cleanPool)
     */
    private int maxPoolIndex = 0;

    /** Number of times this Pool has been cleaned */
    private int numCleans = 0;

    public AABBPool(int par1, int par2)
    {
        this.maxNumCleans = par1;
        this.numEntriesToRemove = par2;
    }

    /**
     * Adds a AABB to the pool, or if there is an available AABB, updates an existing AABB entry to specified
     * coordinates
     */
    public AxisAlignedBB addOrModifyAABBInPool(double par1, double par3, double par5, double par7, double par9, double par11)
    {
        AxisAlignedBB var13;

        if (this.nextPoolIndex >= this.listAABB.size())
        {
            var13 = new AxisAlignedBB(par1, par3, par5, par7, par9, par11);
            this.listAABB.add(var13);
        }
        else
        {
            var13 = (AxisAlignedBB)this.listAABB.get(this.nextPoolIndex);
            var13.setBounds(par1, par3, par5, par7, par9, par11);
        }

        ++this.nextPoolIndex;
        return var13;
    }

    /**
     * Marks the pool as "empty", starting over when adding new entries. If this is called maxNumCleans times, the list
     * size is reduced
     */
    public void cleanPool()
    {
        if (this.nextPoolIndex > this.maxPoolIndex)
        {
            this.maxPoolIndex = this.nextPoolIndex;
        }

        if (this.numCleans++ == this.maxNumCleans)
        {
            int var1 = Math.max(this.maxPoolIndex, this.listAABB.size() - this.numEntriesToRemove);

            while (this.listAABB.size() > var1)
            {
                this.listAABB.remove(var1);
            }

            this.maxPoolIndex = 0;
            this.numCleans = 0;
        }

        this.nextPoolIndex = 0;
    }

    public int getlistAABBsize()
    {
        return this.listAABB.size();
    }

    public int getnextPoolIndex()
    {
        return this.nextPoolIndex;
    }
}
