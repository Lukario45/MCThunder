package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGeneratorOptions implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelGeneratorOptions(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85140_a()
    {
        return WorldInfo.func_85130_c(this.worldInfoInstance);
    }

    public Object call()
    {
        return this.func_85140_a();
    }
}
