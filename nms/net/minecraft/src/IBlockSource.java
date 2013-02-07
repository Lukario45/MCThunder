package net.minecraft.src;

public interface IBlockSource extends ILocatableSource
{
    double getX();

    double getY();

    double getZ();

    int getXInt();

    int getYInt();

    int getZInt();

    int func_82620_h();

    TileEntity func_82619_j();
}
