package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelSpawnLocation implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelSpawnLocation(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85134_a()
    {
        return CrashReportCategory.func_85071_a(WorldInfo.func_85125_d(this.worldInfoInstance), WorldInfo.func_85124_e(this.worldInfoInstance), WorldInfo.func_85123_f(this.worldInfoInstance));
    }

    public Object call()
    {
        return this.func_85134_a();
    }
}
