package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager
{
    /** Reference to the logger. */
    public static Logger logger = Logger.getLogger("Minecraft");

    /** The server properties object. */
    private Properties serverProperties = new Properties();

    /** The server properties file. */
    private File serverPropertiesFile;

    public PropertyManager(File par1File)
    {
        this.serverPropertiesFile = par1File;

        if (par1File.exists())
        {
            FileInputStream var2 = null;

            try
            {
                var2 = new FileInputStream(par1File);
                this.serverProperties.load(var2);
            }
            catch (Exception var12)
            {
                logger.log(Level.WARNING, "Failed to load " + par1File, var12);
                this.generateNewProperties();
            }
            finally
            {
                if (var2 != null)
                {
                    try
                    {
                        var2.close();
                    }
                    catch (IOException var11)
                    {
                        ;
                    }
                }
            }
        }
        else
        {
            logger.log(Level.WARNING, par1File + " does not exist");
            this.generateNewProperties();
        }
    }

    /**
     * Generates a new properties file.
     */
    public void generateNewProperties()
    {
        logger.log(Level.INFO, "Generating new properties file");
        this.saveProperties();
    }

    /**
     * Writes the properties to the properties file.
     */
    public void saveProperties()
    {
        FileOutputStream var1 = null;

        try
        {
            var1 = new FileOutputStream(this.serverPropertiesFile);
            this.serverProperties.store(var1, "Minecraft server properties");
        }
        catch (Exception var11)
        {
            logger.log(Level.WARNING, "Failed to save " + this.serverPropertiesFile, var11);
            this.generateNewProperties();
        }
        finally
        {
            if (var1 != null)
            {
                try
                {
                    var1.close();
                }
                catch (IOException var10)
                {
                    ;
                }
            }
        }
    }

    /**
     * Returns this PropertyManager's file object used for property saving.
     */
    public File getPropertiesFile()
    {
        return this.serverPropertiesFile;
    }

    /**
     * Returns a string property. If the property doesn't exist the default is returned.
     */
    public String getStringProperty(String par1Str, String par2Str)
    {
        if (!this.serverProperties.containsKey(par1Str))
        {
            this.serverProperties.setProperty(par1Str, par2Str);
            this.saveProperties();
        }

        return this.serverProperties.getProperty(par1Str, par2Str);
    }

    /**
     * Gets an integer property. If it does not exist, set it to the specified value.
     */
    public int getIntProperty(String par1Str, int par2)
    {
        try
        {
            return Integer.parseInt(this.getStringProperty(par1Str, "" + par2));
        }
        catch (Exception var4)
        {
            this.serverProperties.setProperty(par1Str, "" + par2);
            return par2;
        }
    }

    /**
     * Gets a boolean property. If it does not exist, set it to the specified value.
     */
    public boolean getBooleanProperty(String par1Str, boolean par2)
    {
        try
        {
            return Boolean.parseBoolean(this.getStringProperty(par1Str, "" + par2));
        }
        catch (Exception var4)
        {
            this.serverProperties.setProperty(par1Str, "" + par2);
            return par2;
        }
    }

    /**
     * Saves an Object with the given property name.
     */
    public void setProperty(String par1Str, Object par2Obj)
    {
        this.serverProperties.setProperty(par1Str, "" + par2Obj);
    }
}
