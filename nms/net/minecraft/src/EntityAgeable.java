package net.minecraft.src;

public abstract class EntityAgeable extends EntityCreature
{
    public EntityAgeable(World par1World)
    {
        super(par1World);
    }

    public abstract EntityAgeable createChild(EntityAgeable var1);

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();

        if (var2 != null && var2.itemID == Item.monsterPlacer.itemID && !this.worldObj.isRemote)
        {
            Class var3 = EntityList.getClassFromID(var2.getItemDamage());

            if (var3 != null && var3.isAssignableFrom(this.getClass()))
            {
                EntityAgeable var4 = this.createChild(this);

                if (var4 != null)
                {
                    var4.setGrowingAge(-24000);
                    var4.setLocationAndAngles(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
                    this.worldObj.spawnEntityInWorld(var4);

                    if (!par1EntityPlayer.capabilities.isCreativeMode)
                    {
                        --var2.stackSize;

                        if (var2.stackSize <= 0)
                        {
                            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                        }
                    }
                }
            }
        }

        return super.interact(par1EntityPlayer);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(12, new Integer(0));
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. Don't confuse this with EntityLiving.getAge. With a negative value the
     * Entity is considered a child.
     */
    public int getGrowingAge()
    {
        return this.dataWatcher.getWatchableObjectInt(12);
    }

    /**
     * The age value may be negative or positive or zero. If it's negative, it get's incremented on each tick, if it's
     * positive, it get's decremented each tick. With a negative value the Entity is considered a child.
     */
    public void setGrowingAge(int par1)
    {
        this.dataWatcher.updateObject(12, Integer.valueOf(par1));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Age", this.getGrowingAge());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        this.setGrowingAge(par1NBTTagCompound.getInteger("Age"));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        int var1 = this.getGrowingAge();

        if (var1 < 0)
        {
            ++var1;
            this.setGrowingAge(var1);
        }
        else if (var1 > 0)
        {
            --var1;
            this.setGrowingAge(var1);
        }
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return this.getGrowingAge() < 0;
    }
}
