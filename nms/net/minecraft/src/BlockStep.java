package net.minecraft.src;

import java.util.Random;

public class BlockStep extends BlockHalfSlab
{
    /** The list of the types of step blocks. */
    public static final String[] blockStepTypes = new String[] {"stone", "sand", "wood", "cobble", "brick", "smoothStoneBrick", "netherBrick"};

    public BlockStep(int par1, boolean par2)
    {
        super(par1, par2, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        int var3 = par2 & 7;
        return var3 == 0 ? (par1 <= 1 ? 6 : 5) : (var3 == 1 ? (par1 == 0 ? 208 : (par1 == 1 ? 176 : 192)) : (var3 == 2 ? 4 : (var3 == 3 ? 16 : (var3 == 4 ? Block.brick.blockIndexInTexture : (var3 == 5 ? Block.stoneBrick.blockIndexInTexture : (var3 == 6 ? Block.netherBrick.blockIndexInTexture : 6))))));
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int par1)
    {
        return this.getBlockTextureFromSideAndMetadata(par1, 0);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.stoneSingleSlab.blockID;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int par1)
    {
        return new ItemStack(Block.stoneSingleSlab.blockID, 2, par1 & 7);
    }

    /**
     * Returns the slab block name with step type.
     */
    public String getFullSlabName(int par1)
    {
        if (par1 < 0 || par1 >= blockStepTypes.length)
        {
            par1 = 0;
        }

        return super.getBlockName() + "." + blockStepTypes[par1];
    }
}
