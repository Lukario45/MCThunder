package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableServerMemoryStats implements Callable
{
    final MinecraftServer field_92075_a;

    public CallableServerMemoryStats(MinecraftServer par1MinecraftServer)
    {
        this.field_92075_a = par1MinecraftServer;
    }

    public String func_92074_a()
    {
        return MinecraftServer.getServerConfigurationManager(this.field_92075_a).getCurrentPlayerCount() + " / " + MinecraftServer.getServerConfigurationManager(this.field_92075_a).getMaxPlayers() + "; " + MinecraftServer.getServerConfigurationManager(this.field_92075_a).playerEntityList;
    }

    public Object call()
    {
        return this.func_92074_a();
    }
}
