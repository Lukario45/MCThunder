package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelSeed implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelSeed(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85142_a()
    {
        return String.valueOf(this.worldInfoInstance.getSeed());
    }

    public Object call()
    {
        return this.func_85142_a();
    }
}
