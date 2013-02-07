package net.minecraft.src;

import java.util.Random;

public class BlockEnderChest extends BlockContainer
{
    protected BlockEnderChest(int par1)
    {
        super(par1, Material.rock);
        this.blockIndexInTexture = 37;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
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
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 22;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.obsidian.blockID;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 8;
    }

    /**
     * Return true if a player with Silk Touch can harvest this block directly, and not its normal drops.
     */
    protected boolean canSilkHarvest()
    {
        return true;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        byte var6 = 0;
        int var7 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var7 == 0)
        {
            var6 = 2;
        }

        if (var7 == 1)
        {
            var6 = 5;
        }

        if (var7 == 2)
        {
            var6 = 3;
        }

        if (var7 == 3)
        {
            var6 = 4;
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        InventoryEnderChest var10 = par5EntityPlayer.getInventoryEnderChest();
        TileEntityEnderChest var11 = (TileEntityEnderChest)par1World.getBlockTileEntity(par2, par3, par4);

        if (var10 != null && var11 != null)
        {
            if (par1World.isBlockNormalCube(par2, par3 + 1, par4))
            {
                return true;
            }
            else if (par1World.isRemote)
            {
                return true;
            }
            else
            {
                var10.setAssociatedChest(var11);
                par5EntityPlayer.displayGUIChest(var10);
                return true;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World par1World)
    {
        return new TileEntityEnderChest();
    }
}
