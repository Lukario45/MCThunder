package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadMinecraftServer extends Thread
{
    /** Reference to the MinecraftServer object. */
    final MinecraftServer mcServer;

    public ThreadMinecraftServer(MinecraftServer par1MinecraftServer, String par2Str)
    {
        super(par2Str);
        this.mcServer = par1MinecraftServer;
    }

    public void run()
    {
        this.mcServer.run();
    }
}
