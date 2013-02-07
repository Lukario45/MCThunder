package net.minecraft.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public abstract class NetworkListenThread
{
    /** Reference to the logger. */
    public static Logger logger = Logger.getLogger("Minecraft");

    /** Reference to the MinecraftServer object. */
    private final MinecraftServer mcServer;
    private final List connections = Collections.synchronizedList(new ArrayList());

    /** Whether the network listener object is listening. */
    public volatile boolean isListening = false;

    public NetworkListenThread(MinecraftServer par1MinecraftServer) throws IOException
    {
        this.mcServer = par1MinecraftServer;
        this.isListening = true;
    }

    /**
     * adds this connection to the list of currently connected players
     */
    public void addPlayer(NetServerHandler par1NetServerHandler)
    {
        this.connections.add(par1NetServerHandler);
    }

    public void stopListening()
    {
        this.isListening = false;
    }

    /**
     * Handles all incoming connections and packets
     */
    public void handleNetworkListenThread()
    {
        for (int var1 = 0; var1 < this.connections.size(); ++var1)
        {
            NetServerHandler var2 = (NetServerHandler)this.connections.get(var1);

            try
            {
                var2.handlePackets();
            }
            catch (Exception var5)
            {
                if (var2.netManager instanceof MemoryConnection)
                {
                    CrashReport var4 = CrashReport.makeCrashReport(var5, "Ticking memory connection");
                    throw new ReportedException(var4);
                }

                logger.log(Level.WARNING, "Failed to handle packet for " + var2.playerEntity.getEntityName() + "/" + var2.playerEntity.func_71114_r() + ": " + var5, var5);
                var2.kickPlayer("Internal server error");
            }

            if (var2.connectionClosed)
            {
                this.connections.remove(var1--);
            }

            var2.netManager.wakeThreads();
        }
    }

    public MinecraftServer getServer()
    {
        return this.mcServer;
    }
}
