package net.minecraft.src;

import java.util.concurrent.Callable;

final class CallableBlockLocation implements Callable
{
    final int field_85067_a;

    final int field_85065_b;

    final int field_85066_c;

    CallableBlockLocation(int par1, int par2, int par3)
    {
        this.field_85067_a = par1;
        this.field_85065_b = par2;
        this.field_85066_c = par3;
    }

    public String func_85064_a()
    {
        return CrashReportCategory.func_85071_a(this.field_85067_a, this.field_85065_b, this.field_85066_c);
    }

    public Object call()
    {
        return this.func_85064_a();
    }
}
