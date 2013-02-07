package net.minecraft.src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

final class ServerWindowAdapter extends WindowAdapter
{
    /** The Minecraft instance. */
    final DedicatedServer mc;

    ServerWindowAdapter(DedicatedServer par1DedicatedServer)
    {
        this.mc = par1DedicatedServer;
    }

    public void windowClosing(WindowEvent par1WindowEvent)
    {
        this.mc.initiateShutdown();

        while (!this.mc.isServerStopped())
        {
            try
            {
                Thread.sleep(100L);
            }
            catch (InterruptedException var3)
            {
                var3.printStackTrace();
            }
        }

        System.exit(0);
    }
}
