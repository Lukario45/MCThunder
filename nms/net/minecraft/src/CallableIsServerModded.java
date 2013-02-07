package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableIsServerModded implements Callable
{
    final MinecraftServer mcServer;

    public CallableIsServerModded(MinecraftServer par1MinecraftServer)
    {
        this.mcServer = par1MinecraftServer;
    }

    public String func_82554_a()
    {
        return this.mcServer.theProfiler.profilingEnabled ? this.mcServer.theProfiler.getNameOfLastSection() : "N/A (disabled)";
    }

    public Object call()
    {
        return this.func_82554_a();
    }
}
