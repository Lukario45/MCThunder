package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import net.minecraft.server.MinecraftServer;

public class DedicatedServer extends MinecraftServer implements IServer
{
    private final List pendingCommandList = Collections.synchronizedList(new ArrayList());
    private RConThreadQuery theRConThreadQuery;
    private RConThreadMain theRConThreadMain;
    private PropertyManager settings;
    private boolean canSpawnStructures;

    /** The Game Type. */
    private EnumGameType theGameType;
    private NetworkListenThread networkThread;
    private boolean guiIsEnabled = false;

    public DedicatedServer(File par1File)
    {
        super(par1File);
        new DedicatedServerSleepThread(this);
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        DedicatedServerCommandThread var1 = new DedicatedServerCommandThread(this);
        var1.setDaemon(true);
        var1.start();
        ConsoleLogManager.init();
        logger.info("Starting minecraft server version 1.4.7");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        logger.info("Loading properties");
        this.settings = new PropertyManager(new File("server.properties"));

        if (this.isSinglePlayer())
        {
            this.setHostname("127.0.0.1");
        }
        else
        {
            this.setOnlineMode(this.settings.getBooleanProperty("online-mode", true));
            this.setHostname(this.settings.getStringProperty("server-ip", ""));
        }

        this.setCanSpawnAnimals(this.settings.getBooleanProperty("spawn-animals", true));
        this.setCanSpawnNPCs(this.settings.getBooleanProperty("spawn-npcs", true));
        this.setAllowPvp(this.settings.getBooleanProperty("pvp", true));
        this.setAllowFlight(this.settings.getBooleanProperty("allow-flight", false));
        this.setTexturePack(this.settings.getStringProperty("texture-pack", ""));
        this.setMOTD(this.settings.getStringProperty("motd", "A Minecraft Server"));

        if (this.settings.getIntProperty("difficulty", 1) < 0)
        {
            this.settings.setProperty("difficulty", Integer.valueOf(0));
        }
        else if (this.settings.getIntProperty("difficulty", 1) > 3)
        {
            this.settings.setProperty("difficulty", Integer.valueOf(3));
        }

        this.canSpawnStructures = this.settings.getBooleanProperty("generate-structures", true);
        int var2 = this.settings.getIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
        this.theGameType = WorldSettings.getGameTypeById(var2);
        logger.info("Default game type: " + this.theGameType);
        InetAddress var3 = null;

        if (this.getServerHostname().length() > 0)
        {
            var3 = InetAddress.getByName(this.getServerHostname());
        }

        if (this.getServerPort() < 0)
        {
            this.setServerPort(this.settings.getIntProperty("server-port", 25565));
        }

        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        logger.info("Starting Minecraft server on " + (this.getServerHostname().length() == 0 ? "*" : this.getServerHostname()) + ":" + this.getServerPort());

        try
        {
            this.networkThread = new DedicatedServerListenThread(this, var3, this.getServerPort());
        }
        catch (IOException var16)
        {
            logger.warning("**** FAILED TO BIND TO PORT!");
            logger.log(Level.WARNING, "The exception was: " + var16.toString());
            logger.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!this.isServerInOnlineMode())
        {
            logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            logger.warning("The server will make no attempt to authenticate usernames. Beware.");
            logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            logger.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        this.setConfigurationManager(new DedicatedPlayerList(this));
        long var4 = System.nanoTime();

        if (this.getFolderName() == null)
        {
            this.setFolderName(this.settings.getStringProperty("level-name", "world"));
        }

        String var6 = this.settings.getStringProperty("level-seed", "");
        String var7 = this.settings.getStringProperty("level-type", "DEFAULT");
        String var8 = this.settings.getStringProperty("generator-settings", "");
        long var9 = (new Random()).nextLong();

        if (var6.length() > 0)
        {
            try
            {
                long var11 = Long.parseLong(var6);

                if (var11 != 0L)
                {
                    var9 = var11;
                }
            }
            catch (NumberFormatException var15)
            {
                var9 = (long)var6.hashCode();
            }
        }

        WorldType var17 = WorldType.parseWorldType(var7);

        if (var17 == null)
        {
            var17 = WorldType.DEFAULT;
        }

        this.setBuildLimit(this.settings.getIntProperty("max-build-height", 256));
        this.setBuildLimit((this.getBuildLimit() + 8) / 16 * 16);
        this.setBuildLimit(MathHelper.clamp_int(this.getBuildLimit(), 64, 256));
        this.settings.setProperty("max-build-height", Integer.valueOf(this.getBuildLimit()));
        logger.info("Preparing level \"" + this.getFolderName() + "\"");
        this.loadAllWorlds(this.getFolderName(), this.getFolderName(), var9, var17, var8);
        long var12 = System.nanoTime() - var4;
        String var14 = String.format("%.3fs", new Object[] {Double.valueOf((double)var12 / 1.0E9D)});
        logger.info("Done (" + var14 + ")! For help, type \"help\" or \"?\"");

        if (this.settings.getBooleanProperty("enable-query", false))
        {
            logger.info("Starting GS4 status listener");
            this.theRConThreadQuery = new RConThreadQuery(this);
            this.theRConThreadQuery.startThread();
        }

        if (this.settings.getBooleanProperty("enable-rcon", false))
        {
            logger.info("Starting remote control listener");
            this.theRConThreadMain = new RConThreadMain(this);
            this.theRConThreadMain.startThread();
        }

        return true;
    }

    public boolean canStructuresSpawn()
    {
        return this.canSpawnStructures;
    }

    public EnumGameType getGameType()
    {
        return this.theGameType;
    }

    /**
     * Defaults to "1" (Easy) for the dedicated server, defaults to "2" (Normal) on the client.
     */
    public int getDifficulty()
    {
        return this.settings.getIntProperty("difficulty", 1);
    }

    /**
     * Defaults to false.
     */
    public boolean isHardcore()
    {
        return this.settings.getBooleanProperty("hardcore", false);
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        while (this.isServerRunning())
        {
            this.executePendingCommands();

            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException var3)
            {
                var3.printStackTrace();
            }
        }
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.func_85056_g().addCrashSectionCallable("Is Modded", new CallableType(this));
        par1CrashReport.func_85056_g().addCrashSectionCallable("Type", new CallableServerType(this));
        return par1CrashReport;
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    protected void systemExitNow()
    {
        System.exit(0);
    }

    public void updateTimeLightAndEntities()
    {
        super.updateTimeLightAndEntities();
        this.executePendingCommands();
    }

    public boolean getAllowNether()
    {
        return this.settings.getBooleanProperty("allow-nether", true);
    }

    public boolean allowSpawnMonsters()
    {
        return this.settings.getBooleanProperty("spawn-monsters", true);
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(this.getDedicatedPlayerList().isWhiteListEnabled()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(this.getDedicatedPlayerList().getWhiteListedPlayers().size()));
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return this.settings.getBooleanProperty("snooper-enabled", true);
    }

    public void addPendingCommand(String par1Str, ICommandSender par2ICommandSender)
    {
        this.pendingCommandList.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void executePendingCommands()
    {
        while (!this.pendingCommandList.isEmpty())
        {
            ServerCommand var1 = (ServerCommand)this.pendingCommandList.remove(0);
            this.getCommandManager().executeCommand(var1.sender, var1.command);
        }
    }

    public boolean isDedicatedServer()
    {
        return true;
    }

    public DedicatedPlayerList getDedicatedPlayerList()
    {
        return (DedicatedPlayerList)super.getConfigurationManager();
    }

    public NetworkListenThread getNetworkThread()
    {
        return this.networkThread;
    }

    /**
     * Gets an integer property. If it does not exist, set it to the specified value.
     */
    public int getIntProperty(String par1Str, int par2)
    {
        return this.settings.getIntProperty(par1Str, par2);
    }

    /**
     * Gets a string property. If it does not exist, set it to the specified value.
     */
    public String getStringProperty(String par1Str, String par2Str)
    {
        return this.settings.getStringProperty(par1Str, par2Str);
    }

    /**
     * Gets a boolean property. If it does not exist, set it to the specified value.
     */
    public boolean getBooleanProperty(String par1Str, boolean par2)
    {
        return this.settings.getBooleanProperty(par1Str, par2);
    }

    /**
     * Saves an Object with the given property name.
     */
    public void setProperty(String par1Str, Object par2Obj)
    {
        this.settings.setProperty(par1Str, par2Obj);
    }

    /**
     * Saves all of the server properties to the properties file.
     */
    public void saveProperties()
    {
        this.settings.saveProperties();
    }

    /**
     * Returns the filename where server properties are stored
     */
    public String getSettingsFilename()
    {
        File var1 = this.settings.getPropertiesFile();
        return var1 != null ? var1.getAbsolutePath() : "No settings file";
    }

    public void enableGui()
    {
        ServerGUI.initGUI(this);
        this.guiIsEnabled = true;
    }

    public boolean getGuiEnabled()
    {
        return this.guiIsEnabled;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    /**
     * Return whether command blocks are enabled.
     */
    public boolean isCommandBlockEnabled()
    {
        return this.settings.getBooleanProperty("enable-command-block", false);
    }

    /**
     * Return the spawn protection area's size.
     */
    public int getSpawnProtectionSize()
    {
        return this.settings.getIntProperty("spawn-protection", super.getSpawnProtectionSize());
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.getDedicatedPlayerList();
    }
}
