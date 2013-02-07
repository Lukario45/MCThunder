package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableStructureType implements Callable
{
    final MapGenStructure field_85161_a;

    CallableStructureType(MapGenStructure par1MapGenStructure)
    {
        this.field_85161_a = par1MapGenStructure;
    }

    public String func_85160_a()
    {
        return this.field_85161_a.getClass().getCanonicalName();
    }

    public Object call()
    {
        return this.func_85160_a();
    }
}
