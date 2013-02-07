package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelDimension implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelDimension(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85114_a()
    {
        return String.valueOf(WorldInfo.func_85122_i(this.worldInfoInstance));
    }

    public Object call()
    {
        return this.func_85114_a();
    }
}
