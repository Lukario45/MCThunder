package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGenerator implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelGenerator(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85138_a()
    {
        return String.format("ID %02d - %s, ver %d. Features enabled: %b", new Object[] {Integer.valueOf(WorldInfo.getTerrainTypeOfWorld(this.worldInfoInstance).getWorldTypeID()), WorldInfo.getTerrainTypeOfWorld(this.worldInfoInstance).getWorldTypeName(), Integer.valueOf(WorldInfo.getTerrainTypeOfWorld(this.worldInfoInstance).getGeneratorVersion()), Boolean.valueOf(WorldInfo.getMapFeaturesEnabled(this.worldInfoInstance))});
    }

    public Object call()
    {
        return this.func_85138_a();
    }
}
