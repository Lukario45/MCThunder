package net.minecraft.src;

class TileEntityMobSpawnerSpawnData extends WeightedRandomItem
{
    public final NBTTagCompound field_92083_b;
    public final String field_92084_c;

    final TileEntityMobSpawner field_92082_d;

    public TileEntityMobSpawnerSpawnData(TileEntityMobSpawner par1TileEntityMobSpawner, NBTTagCompound par2NBTTagCompound)
    {
        super(par2NBTTagCompound.getInteger("Weight"));
        this.field_92082_d = par1TileEntityMobSpawner;
        this.field_92083_b = par2NBTTagCompound.getCompoundTag("Properties");
        this.field_92084_c = par2NBTTagCompound.getString("Type");
    }

    public TileEntityMobSpawnerSpawnData(TileEntityMobSpawner par1TileEntityMobSpawner, NBTTagCompound par2NBTTagCompound, String par3Str)
    {
        super(1);
        this.field_92082_d = par1TileEntityMobSpawner;
        this.field_92083_b = par2NBTTagCompound;
        this.field_92084_c = par3Str;
    }

    public NBTTagCompound func_92081_a()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setCompoundTag("Properties", this.field_92083_b);
        var1.setString("Type", this.field_92084_c);
        var1.setInteger("Weight", this.itemWeight);
        return var1;
    }
}
