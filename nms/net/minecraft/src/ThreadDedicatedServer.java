package net.minecraft.src;

public final class ThreadDedicatedServer extends Thread
{
    final DedicatedServer field_92021_a;

    public ThreadDedicatedServer(DedicatedServer par1DedicatedServer)
    {
        this.field_92021_a = par1DedicatedServer;
    }

    public void run()
    {
        this.field_92021_a.stopServer();
    }
}
