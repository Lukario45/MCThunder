package net.minecraft.src;

import java.util.Random;

public class BlockRail extends Block
{
    /** Power related rails have this field at true. */
    private final boolean isPowered;

    /**
     * Returns true if the block at the coordinates of world passed is a valid rail block (current is rail, powered or
     * detector).
     */
    public static final boolean isRailBlockAt(World par0World, int par1, int par2, int par3)
    {
        int var4 = par0World.getBlockId(par1, par2, par3);
        return var4 == Block.rail.blockID || var4 == Block.railPowered.blockID || var4 == Block.railDetector.blockID;
    }

    /**
     * Return true if the parameter is a blockID for a valid rail block (current is rail, powered or detector).
     */
    public static final boolean isRailBlock(int par0)
    {
        return par0 == Block.rail.blockID || par0 == Block.railPowered.blockID || par0 == Block.railDetector.blockID;
    }

    protected BlockRail(int par1, int par2, boolean par3)
    {
        super(par1, par2, Material.circuits);
        this.isPowered = par3;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    /**
     * Returns true if the block is power related rail.
     */
    public boolean isPowered()
    {
        return this.isPowered;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
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
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int var5 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (var5 >= 2 && var5 <= 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (this.isPowered)
        {
            if (this.blockID == Block.railPowered.blockID && (par2 & 8) == 0)
            {
                return this.blockIndexInTexture - 16;
            }
        }
        else if (par2 >= 6)
        {
            return this.blockIndexInTexture - 16;
        }

        return this.blockIndexInTexture;
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
        return 9;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote)
        {
            this.refreshTrackShape(par1World, par2, par3, par4, true);

            if (this.blockID == Block.railPowered.blockID)
            {
                this.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote)
        {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);
            int var7 = var6;

            if (this.isPowered)
            {
                var7 = var6 & 7;
            }

            boolean var8 = false;

            if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
            {
                var8 = true;
            }

            if (var7 == 2 && !par1World.doesBlockHaveSolidTopSurface(par2 + 1, par3, par4))
            {
                var8 = true;
            }

            if (var7 == 3 && !par1World.doesBlockHaveSolidTopSurface(par2 - 1, par3, par4))
            {
                var8 = true;
            }

            if (var7 == 4 && !par1World.doesBlockHaveSolidTopSurface(par2, par3, par4 - 1))
            {
                var8 = true;
            }

            if (var7 == 5 && !par1World.doesBlockHaveSolidTopSurface(par2, par3, par4 + 1))
            {
                var8 = true;
            }

            if (var8)
            {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
            else if (this.blockID == Block.railPowered.blockID)
            {
                boolean var9 = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
                var9 = var9 || this.isNeighborRailPowered(par1World, par2, par3, par4, var6, true, 0) || this.isNeighborRailPowered(par1World, par2, par3, par4, var6, false, 0);
                boolean var10 = false;

                if (var9 && (var6 & 8) == 0)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var7 | 8);
                    var10 = true;
                }
                else if (!var9 && (var6 & 8) != 0)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var7);
                    var10 = true;
                }

                if (var10)
                {
                    par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);

                    if (var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5)
                    {
                        par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
                    }
                }
            }
            else if (par5 > 0 && Block.blocksList[par5].canProvidePower() && !this.isPowered && RailLogic.getAdjacentTrackCount(new RailLogic(this, par1World, par2, par3, par4)) == 3)
            {
                this.refreshTrackShape(par1World, par2, par3, par4, false);
            }
        }
    }

    /**
     * Completely recalculates the track shape based on neighboring tracks
     */
    private void refreshTrackShape(World par1World, int par2, int par3, int par4, boolean par5)
    {
        if (!par1World.isRemote)
        {
            (new RailLogic(this, par1World, par2, par3, par4)).refreshTrackShape(par1World.isBlockIndirectlyGettingPowered(par2, par3, par4), par5);
        }
    }

    /**
     * Powered minecart rail is conductive like wire, so check for powered neighbors
     */
    private boolean isNeighborRailPowered(World par1World, int par2, int par3, int par4, int par5, boolean par6, int par7)
    {
        if (par7 >= 8)
        {
            return false;
        }
        else
        {
            int var8 = par5 & 7;
            boolean var9 = true;

            switch (var8)
            {
                case 0:
                    if (par6)
                    {
                        ++par4;
                    }
                    else
                    {
                        --par4;
                    }

                    break;

                case 1:
                    if (par6)
                    {
                        --par2;
                    }
                    else
                    {
                        ++par2;
                    }

                    break;

                case 2:
                    if (par6)
                    {
                        --par2;
                    }
                    else
                    {
                        ++par2;
                        ++par3;
                        var9 = false;
                    }

                    var8 = 1;
                    break;

                case 3:
                    if (par6)
                    {
                        --par2;
                        ++par3;
                        var9 = false;
                    }
                    else
                    {
                        ++par2;
                    }

                    var8 = 1;
                    break;

                case 4:
                    if (par6)
                    {
                        ++par4;
                    }
                    else
                    {
                        --par4;
                        ++par3;
                        var9 = false;
                    }

                    var8 = 0;
                    break;

                case 5:
                    if (par6)
                    {
                        ++par4;
                        ++par3;
                        var9 = false;
                    }
                    else
                    {
                        --par4;
                    }

                    var8 = 0;
            }

            return this.isRailPassingPower(par1World, par2, par3, par4, par6, par7, var8) ? true : var9 && this.isRailPassingPower(par1World, par2, par3 - 1, par4, par6, par7, var8);
        }
    }

    /**
     * Returns true if the specified rail is passing power to its neighbor
     */
    private boolean isRailPassingPower(World par1World, int par2, int par3, int par4, boolean par5, int par6, int par7)
    {
        int var8 = par1World.getBlockId(par2, par3, par4);

        if (var8 == Block.railPowered.blockID)
        {
            int var9 = par1World.getBlockMetadata(par2, par3, par4);
            int var10 = var9 & 7;

            if (par7 == 1 && (var10 == 0 || var10 == 4 || var10 == 5))
            {
                return false;
            }

            if (par7 == 0 && (var10 == 1 || var10 == 2 || var10 == 3))
            {
                return false;
            }

            if ((var9 & 8) != 0)
            {
                if (par1World.isBlockIndirectlyGettingPowered(par2, par3, par4))
                {
                    return true;
                }

                return this.isNeighborRailPowered(par1World, par2, par3, par4, var9, par5, par6 + 1);
            }
        }

        return false;
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int getMobilityFlag()
    {
        return 0;
    }

    /**
     * Return true if the blocks passed is a power related rail.
     */
    static boolean isPoweredBlockRail(BlockRail par0BlockRail)
    {
        return par0BlockRail.isPowered;
    }
}
