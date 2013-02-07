package net.minecraft.src;

public class BlockSourceImpl implements IBlockSource
{
    private final World worldObj;
    private final int xPos;
    private final int yPos;
    private final int zPos;

    public BlockSourceImpl(World par1World, int par2, int par3, int par4)
    {
        this.worldObj = par1World;
        this.xPos = par2;
        this.yPos = par3;
        this.zPos = par4;
    }

    public World getWorld()
    {
        return this.worldObj;
    }

    public double getX()
    {
        return (double)this.xPos + 0.5D;
    }

    public double getY()
    {
        return (double)this.yPos + 0.5D;
    }

    public double getZ()
    {
        return (double)this.zPos + 0.5D;
    }

    public int getXInt()
    {
        return this.xPos;
    }

    public int getYInt()
    {
        return this.yPos;
    }

    public int getZInt()
    {
        return this.zPos;
    }

    public int func_82620_h()
    {
        return this.worldObj.getBlockMetadata(this.xPos, this.yPos, this.zPos);
    }

    public TileEntity func_82619_j()
    {
        return this.worldObj.getBlockTileEntity(this.xPos, this.yPos, this.zPos);
    }
}
