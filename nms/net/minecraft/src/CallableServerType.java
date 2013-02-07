package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableServerType implements Callable
{
    final DedicatedServer field_85171_a;

    CallableServerType(DedicatedServer par1DedicatedServer)
    {
        this.field_85171_a = par1DedicatedServer;
    }

    public String func_85170_a()
    {
        return "Dedicated Server (map_server.txt)";
    }

    public Object call()
    {
        return this.func_85170_a();
    }
}
