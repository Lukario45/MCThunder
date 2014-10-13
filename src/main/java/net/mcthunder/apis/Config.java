package net.mcthunder.apis;

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
    private String motd;
    private String world;
    private String serverName;
    private String host;
    private String globalChatNick;
    private String globalNickPassword;
    private int slots;
    private int port;

    //Load Config
    public void loadConfig() {
        try {
            //Make Config
            String path = "config.yml";
            //String pathDecoded = URLDecoder.decode(path, "UTF-8");
            setConf(new PropertiesConfiguration(new File(path)));

            //See if the Config Exists yet. If not make it
            if (!getConf().getFile().exists()) {
                Utils.tellConsole("INFO", "Creating Server Configuration at " + path);
                getConf().getFile().createNewFile();
                getConf().setProperty("SERVER-NAME", "A MCThunder Minecraft Server");
                getConf().setProperty("SERVER-HOST", "127.0.0.1");
                getConf().setProperty("SERVER-PORT", 25565);
                getConf().setProperty("PLAYER-SLOTS", 20);
                getConf().setProperty("ENABLE-GLOBAL", true);
                getConf().setProperty("GLOBAL-NICK", "CHANGEME");
                getConf().setProperty("GLOBAL-NICK-REGISTERED", false);
                getConf().setProperty("GLOBAL-NICK-PASSWORD", "password");
                getConf().setProperty("WORLD-NAME", "world");
                getConf().setProperty("SERVER-MOTD", "A MCThunder Minecraft Server");
                getConf().setProperty("SERVER-ONLINE-MODE", false);
                getConf().setProperty("ALLOW-FLYING", false);
                getConf().setProperty("ALLOW-NETHER", true);
                getConf().setProperty("ALLOW-PVP", true);
                getConf().setProperty("USE-COMMAND-BLOCKS", false);
                getConf().save();
            }
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
            setCommandBlocks(getConf().getBoolean("USE-COMMAND-BLOCKS"));

        } catch (ConfigurationException e) {
            Utils.tellConsole("ERROR", "Check Config File!");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Utils.tellConsole("ERROR", "Change your server location!");
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
}
