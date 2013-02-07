package net.minecraft.src;

import java.util.Random;

public class BlockWoodSlab extends BlockHalfSlab
{
    /** The type of tree this slab came from. */
    public static final String[] woodType = new String[] {"oak", "spruce", "birch", "jungle"};

    public BlockWoodSlab(int par1, boolean par2)
    {
        super(par1, par2, Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        switch (par2 & 7)
        {
            case 1:
                return 198;

            case 2:
                return 214;

            case 3:
                return 199;

            default:
                return 4;
        }
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
        return Block.woodSingleSlab.blockID;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int par1)
    {
        return new ItemStack(Block.woodSingleSlab.blockID, 2, par1 & 7);
    }

    /**
     * Returns the slab block name with step type.
     */
    public String getFullSlabName(int par1)
    {
        if (par1 < 0 || par1 >= woodType.length)
        {
            par1 = 0;
        }

        return super.getBlockName() + "." + woodType[par1];
    }
}
