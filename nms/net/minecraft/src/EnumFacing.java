package net.minecraft.src;

public enum EnumFacing
{
    DOWN(0, 1, 0, -1, 0),
    UP(1, 0, 0, 1, 0),
    NORTH(2, 3, 0, 0, -1),
    SOUTH(3, 2, 0, 0, 1),
    EAST(4, 5, -1, 0, 0),
    WEST(5, 4, 1, 0, 0);
    private final int field_82603_g;
    private final int field_82613_h;
    private final int frontOffsetX;
    private final int frontOffsetY;
    private final int frontOffsetZ;
    private static final EnumFacing[] field_82609_l = new EnumFacing[6];

    private EnumFacing(int par3, int par4, int par5, int par6, int par7)
    {
        this.field_82603_g = par3;
        this.field_82613_h = par4;
        this.frontOffsetX = par5;
        this.frontOffsetY = par6;
        this.frontOffsetZ = par7;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetX()
    {
        return this.frontOffsetX;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetZ()
    {
        return this.frontOffsetZ;
    }

    /**
     * Returns the facing that represents the block in front of it.
     */
    public static EnumFacing getFront(int par0)
    {
        return field_82609_l[par0 % field_82609_l.length];
    }

    static {
        EnumFacing[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            EnumFacing var3 = var0[var2];
            field_82609_l[var3.field_82603_g] = var3;
        }
    }
}
