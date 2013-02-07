package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelStorageVersion implements Callable
{
    final WorldInfo worldInfoInstance;

    CallableLevelStorageVersion(WorldInfo par1WorldInfo)
    {
        this.worldInfoInstance = par1WorldInfo;
    }

    public String func_85112_a()
    {
        String var1 = "Unknown?";

        try
        {
            switch (WorldInfo.func_85121_j(this.worldInfoInstance))
            {
                case 19132:
                    var1 = "McRegion";
                    break;

                case 19133:
                    var1 = "Anvil";
            }
        }
        catch (Throwable var3)
        {
            ;
        }

        return String.format("0x%05X - %s", new Object[] {Integer.valueOf(WorldInfo.func_85121_j(this.worldInfoInstance)), var1});
    }

    public Object call()
    {
        return this.func_85112_a();
    }
}
