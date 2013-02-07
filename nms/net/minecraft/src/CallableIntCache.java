package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableIntCache implements Callable
{
    final CrashReport field_85084_a;

    CallableIntCache(CrashReport par1CrashReport)
    {
        this.field_85084_a = par1CrashReport;
    }

    public String func_85083_a()
    {
        return IntCache.func_85144_b();
    }

    public Object call()
    {
        return this.func_85083_a();
    }
}
