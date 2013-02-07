package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelWeather implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelWeather(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85110_a()
    {
        return String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] {Integer.valueOf(WorldInfo.func_85119_k(this.worldInfoInstance)), Boolean.valueOf(WorldInfo.func_85127_l(this.worldInfoInstance)), Integer.valueOf(WorldInfo.func_85133_m(this.worldInfoInstance)), Boolean.valueOf(WorldInfo.func_85116_n(this.worldInfoInstance))});
    }

    public Object call()
    {
        return this.func_85110_a();
    }
}
