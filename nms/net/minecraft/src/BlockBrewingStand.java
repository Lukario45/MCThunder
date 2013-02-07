package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockBrewingStand extends BlockContainer
{
    private Random rand = new Random();

    public BlockBrewingStand(int par1)
    {
        super(par1, Material.iron);
        this.blockIndexInTexture = 157;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 25;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityBrewingStand();
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * if the specified block is in the given AABB, add its collision bounding box to the given list
     */
    public void addCollidingBlockToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        this.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
        super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        this.setBlockBoundsForItemRender();
        super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityBrewingStand var10 = (TileEntityBrewingStand)par1World.getBlockTileEntity(par2, par3, par4);

            if (var10 != null)
            {
                par5EntityPlayer.displayGUIBrewingStand(var10);
            }

            return true;
        }
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntity var7 = par1World.getBlockTileEntity(par2, par3, par4);

        if (var7 instanceof TileEntityBrewingStand)
        {
            TileEntityBrewingStand var8 = (TileEntityBrewingStand)var7;

            for (int var9 = 0; var9 < var8.getSizeInventory(); ++var9)
            {
                ItemStack var10 = var8.getStackInSlot(var9);

                if (var10 != null)
                {
                    float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var13 = this.rand.nextFloat() * 0.8F + 0.1F;

                    while (var10.stackSize > 0)
                    {
                        int var14 = this.rand.nextInt(21) + 10;

                        if (var14 > var10.stackSize)
                        {
                            var14 = var10.stackSize;
                        }

                        var10.stackSize -= var14;
                        EntityItem var15 = new EntityItem(par1World, (double)((float)par2 + var11), (double)((float)par3 + var12), (double)((float)par4 + var13), new ItemStack(var10.itemID, var14, var10.getItemDamage()));
                        float var16 = 0.05F;
                        var15.motionX = (double)((float)this.rand.nextGaussian() * var16);
                        var15.motionY = (double)((float)this.rand.nextGaussian() * var16 + 0.2F);
                        var15.motionZ = (double)((float)this.rand.nextGaussian() * var16);
                        par1World.spawnEntityInWorld(var15);
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.brewingStand.itemID;
    }
}
