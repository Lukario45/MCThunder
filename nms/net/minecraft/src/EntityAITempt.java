package net.minecraft.src;

public class EntityAITempt extends EntityAIBase
{
    /** The entity using this AI that is tempted by the player. */
    private EntityCreature temptedEntity;
    private float field_75282_b;
    private double field_75283_c;
    private double field_75280_d;
    private double field_75281_e;
    private double field_75278_f;
    private double field_75279_g;

    /** The player that is tempting the entity that is using this AI. */
    private EntityPlayer temptingPlayer;

    /**
     * A counter that is decremented each time the shouldExecute method is called. The shouldExecute method will always
     * return false if delayTemptCounter is greater than 0.
     */
    private int delayTemptCounter = 0;
    private boolean field_75287_j;

    /**
     * This field saves the ID of the items that can be used to breed entities with this behaviour.
     */
    private int breedingFood;

    /**
     * Whether the entity using this AI will be scared by the tempter's sudden movement.
     */
    private boolean scaredByPlayerMovement;
    private boolean field_75286_m;

    public EntityAITempt(EntityCreature par1EntityCreature, float par2, int par3, boolean par4)
    {
        this.temptedEntity = par1EntityCreature;
        this.field_75282_b = par2;
        this.breedingFood = par3;
        this.scaredByPlayerMovement = par4;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.delayTemptCounter > 0)
        {
            --this.delayTemptCounter;
            return false;
        }
        else
        {
            this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0D);

            if (this.temptingPlayer == null)
            {
                return false;
            }
            else
            {
                ItemStack var1 = this.temptingPlayer.getCurrentEquippedItem();
                return var1 == null ? false : var1.itemID == this.breedingFood;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (this.scaredByPlayerMovement)
        {
            if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 36.0D)
            {
                if (this.temptingPlayer.getDistanceSq(this.field_75283_c, this.field_75280_d, this.field_75281_e) > 0.010000000000000002D)
                {
                    return false;
                }

                if (Math.abs((double)this.temptingPlayer.rotationPitch - this.field_75278_f) > 5.0D || Math.abs((double)this.temptingPlayer.rotationYaw - this.field_75279_g) > 5.0D)
                {
                    return false;
                }
            }
            else
            {
                this.field_75283_c = this.temptingPlayer.posX;
                this.field_75280_d = this.temptingPlayer.posY;
                this.field_75281_e = this.temptingPlayer.posZ;
            }

            this.field_75278_f = (double)this.temptingPlayer.rotationPitch;
            this.field_75279_g = (double)this.temptingPlayer.rotationYaw;
        }

        return this.shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75283_c = this.temptingPlayer.posX;
        this.field_75280_d = this.temptingPlayer.posY;
        this.field_75281_e = this.temptingPlayer.posZ;
        this.field_75287_j = true;
        this.field_75286_m = this.temptedEntity.getNavigator().getAvoidsWater();
        this.temptedEntity.getNavigator().setAvoidsWater(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.temptingPlayer = null;
        this.temptedEntity.getNavigator().clearPathEntity();
        this.delayTemptCounter = 100;
        this.field_75287_j = false;
        this.temptedEntity.getNavigator().setAvoidsWater(this.field_75286_m);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingPlayer, 30.0F, (float)this.temptedEntity.getVerticalFaceSpeed());

        if (this.temptedEntity.getDistanceSqToEntity(this.temptingPlayer) < 6.25D)
        {
            this.temptedEntity.getNavigator().clearPathEntity();
        }
        else
        {
            this.temptedEntity.getNavigator().tryMoveToEntityLiving(this.temptingPlayer, this.field_75282_b);
        }
    }

    public boolean func_75277_f()
    {
        return this.field_75287_j;
    }
}
