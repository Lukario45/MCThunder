package net.mcthunder.rankmanager;

import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Utils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Kevin on 11/12/2014.
 */
public class Config {
    private PropertiesConfiguration conf;
    private String denyMessage;
    private String denyColor;

    public void loadConfig() {
        try {
            //Make Config
            setConf(new PropertiesConfiguration(new File("RankManager/config.yml")));

            //See if the Config Exists yet. If not make it
            if (!getConf().getFile().exists()) {
                Utils.tellConsole(LoggingLevel.INFO, "Creating RankManager config file.");
                getConf().getFile().createNewFile();
            }
            //Make sure it has all fields
            if (!getConf().containsKey("DENY-MESSAGE"))
                getConf().setProperty("DENY-MESSAGE", "You do not have permission to do this!");
            if (!getConf().containsKey("DENY-COLOR"))
                getConf().setProperty("DENY-COLOR", "&c");

            getConf().save();
            //Set all of the values from the config
            getConf().load();
            setDenyMessage(getConf().getString("DENY-MESSAGE"));
            setDenyColor(getConf().getString("DENY-COLOR"));

        } catch (ConfigurationException e) {
            Utils.tellConsole(LoggingLevel.ERROR, "Check RankManager's Config File!");
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

    public String getDenyMessage() {
        return this.denyMessage;
    }

    public void setDenyMessage(String denyMessage) {
        this.denyMessage = denyMessage;
    }

    public String getDenyColor() {
        return this.denyColor;
    }

    public void setDenyColor(String color) {
        this.denyColor = color;
    }
}