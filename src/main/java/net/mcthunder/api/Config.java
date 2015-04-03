package net.mcthunder.api;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Kevin on 8/10/2014.
 */
public class Config {
    private PropertiesConfiguration conf;
    private boolean onlineMode;
    private boolean nether;
    private boolean commandBlocks;
    private boolean allowFlying;
    private boolean enableGlobalChat;
    private boolean globalNickRegisterd;
    private boolean allowPVP;
    private boolean useRankManager;
    private boolean guiMode;
    private String chatFormat;
    private String motd;
    private String world;
    private String serverName;
    private String host;
    private String globalChatNick;
    private String globalNickPassword;
    private int renderDistance;
    private int slots;
    private int port;


    //Load Config
    public void loadConfig() {
        try {
            //Make Config
            setConf(new PropertiesConfiguration(new File("config.yml")));

            //See if the Config Exists yet. If not make it
            if (!getConf().getFile().exists()) {
                Utils.tellConsole(LoggingLevel.INFO, "Creating server config file.");
                getConf().getFile().createNewFile();
            }
            //Make sure it has all fields
            if (!getConf().containsKey("SERVER-NAME"))
                getConf().setProperty("SERVER-NAME", "A MCThunder Minecraft Server");
            if (!getConf().containsKey("SERVER-HOST"))
                getConf().setProperty("SERVER-HOST", "127.0.0.1");
            if (!getConf().containsKey("SERVER-PORT"))
                getConf().setProperty("SERVER-PORT", 25565);
            if (!getConf().containsKey("PLAYER-SLOTS"))
                getConf().setProperty("PLAYER-SLOTS", 20);
            if (!getConf().containsKey("ENABLE-GLOBAL"))
                getConf().setProperty("ENABLE-GLOBAL", true);
            if (!getConf().containsKey("GLOBAL-NICK"))
                getConf().setProperty("GLOBAL-NICK", "CHANGEME");
            if (!getConf().containsKey("GLOBAL-NICK-REGISTERED"))
                getConf().setProperty("GLOBAL-NICK-REGISTERED", false);
            if (!getConf().containsKey("GLOBAL-NICK-PASSWORD"))
                getConf().setProperty("GLOBAL-NICK-PASSWORD", "password");
            if (!getConf().containsKey("WORLD-NAME"))
                getConf().setProperty("WORLD-NAME", "world");
            if (!getConf().containsKey("SERVER-MOTD"))
                getConf().setProperty("SERVER-MOTD", "A MCThunder Minecraft Server");
            if (!getConf().containsKey("SERVER-ONLINE-MODE"))
                getConf().setProperty("SERVER-ONLINE-MODE", false);
            if (!getConf().containsKey("ALLOW-FLYING"))
                getConf().setProperty("ALLOW-FLYING", false);
            if (!getConf().containsKey("ALLOW-NETHER"))
                getConf().setProperty("ALLOW-NETHER", true);
            if (!getConf().containsKey("ALLOW-PVP"))
                getConf().setProperty("ALLOW-PVP", true);
            if (!getConf().containsKey("MAX-REBDER-DISTANCE"))
                getConf().setProperty("MAX-REBDER-DISTANCE", 9);
            if (!getConf().containsKey("USE-COMMAND-BLOCKS"))
                getConf().setProperty("USE-COMMAND-BLOCKS", false);
            if (!getConf().containsKey("useRankManager"))
                getConf().setProperty("useRankManager", true);
            if (!getConf().containsKey("GUI-MODE"))
                getConf().setProperty("GUI-MODE",true);
            if (!getConf().containsKey("chatFormat"))
                getConf().setProperty("chatFormat", "{WORLD} &e{NAME}:&r {MESSAGE}");
            getConf().save();
            //Set all of the values from the config
            getConf().load();
            setServerName(getConf().getString("SERVER-NAME"));
            setHost(getConf().getString("SERVER-HOST"));
            setPort(getConf().getInt("SERVER-PORT"));
            setSlots(getConf().getInt("PLAYER-SLOTS"));
            setGlobalEnabled(getConf().getBoolean("ENABLE-GLOBAL"));
            setGlobalNick(getConf().getString("GLOBAL-NICK"));
            setGlobalNickRegisterd(getConf().getBoolean("GLOBAL-NICK-REGISTERED"));
            setGlobalNickPassword(getConf().getString("GLOBAL-NICK-PASSWORD"));
            setWorldName(getConf().getString("WORLD-NAME"));
            setServerMOTD(getConf().getString("SERVER-MOTD"));
            setOnlineMode(getConf().getBoolean("SERVER-ONLINE-MODE"));
            setAllowFlying(getConf().getBoolean("ALLOW-FLYING"));
            setAllowNether(getConf().getBoolean("ALLOW-NETHER"));
            setAllowPVP(getConf().getBoolean("ALLOW-PVP"));
            setMaxRenderDistance(getConf().getInt("MAX-REBDER-DISTANCE"));
            setCommandBlocks(getConf().getBoolean("USE-COMMAND-BLOCKS"));
            setUseRankManager(getConf().getBoolean("useRankManager"));
            setGuiMode(getConf().getBoolean("GUI-MODE"));
            setChatFormat(getConf().getString("chatFormat"));
        } catch (ConfigurationException e) {
            Utils.tellConsole(LoggingLevel.ERROR, "Check Config File!");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Utils.tellConsole(LoggingLevel.ERROR, "Change your server location!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PropertiesConfiguration getConf() {
        return conf;
    }

    public void setConf(PropertiesConfiguration conf) {
        this.conf = conf;
    }

    //Configureation Getters
    public String getServerName() {
        return this.serverName;
    }

    //Configureation Setters
    public void setServerName(String name) {
        this.serverName = name;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setMaxRenderDistance(int distance) {
        this.renderDistance = distance;
    }

    public int getRenderDistance() {
        return this.renderDistance;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSlots() {
        return this.slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public boolean getGlobalEnabled() {
        return this.enableGlobalChat;
    }

    public void setGlobalEnabled(boolean enabled) {
        this.enableGlobalChat = enabled;
    }

    public String getGlobalNick() {
        return this.globalChatNick;
    }

    public void setGlobalNick(String nick) {
        this.globalChatNick = nick;
    }

    public boolean getGlobalNickRegistered() {
        return this.globalNickRegisterd;
    }

    public void setChatFormat(String format) {
        this.chatFormat = format;
    }

    public String getChatFormat() {
        return this.chatFormat;
    }

    public String getGlobalNickPassword() {
        return this.globalNickPassword;
    }

    public void setGlobalNickPassword(String password) {
        this.globalNickPassword = password;
    }

    public String getWorldName() {
        return this.world;
    }

    public void setWorldName(String name) {
        this.world = name;
    }

    public String getServerMOTD() {
        return this.motd;
    }

    public void setServerMOTD(String motd) {
        this.motd = motd;
    }

    public boolean getOnlineMode() {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    public boolean getAllowFlying() {
        return this.allowFlying;
    }

    public void setAllowFlying(boolean flying) {
        this.allowFlying = flying;
    }

    public boolean getAllowNether() {
        return this.nether;
    }

    public void setAllowNether(boolean nether) {
        this.nether = nether;
    }

    public boolean getAllowPVP() {
        return this.allowPVP;
    }

    public void setAllowPVP(boolean pvp) {
        this.allowPVP = pvp;
    }

    public boolean getUseCommandBlocks() {
        return this.commandBlocks;
    }

    public void setGlobalNickRegisterd(boolean registered) {
        this.globalNickRegisterd = registered;
    }

    public void setCommandBlocks(boolean commandBlocks) {
        this.commandBlocks = commandBlocks;
    }

    public boolean getUseRankManager() {
        return this.useRankManager;
    }

    public void setUseRankManager(boolean useRankManager) {
        this.useRankManager = useRankManager;
    }

    public boolean getGuiMode(){ return this.guiMode;}

    public void setGuiMode(boolean guiMode){
        this.guiMode = guiMode;
    }
}