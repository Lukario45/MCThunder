package net.mcthunder.src;

import java.io.File;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class Congif 
{
	private static PropertiesConfiguration conf;
	public static File cnf = new File("/config.yml");
	private int port;
	private String serverName;
	private String worldName;
	private boolean onlineMode;
	private String ownerName;
	

	public static void loadConfig() throws ConfigurationException, IOException
	{
		conf = new PropertiesConfiguration(cnf);
		if (cnf.exists()) {
			System.out.println(cnf.getAbsolutePath());
			conf.load(cnf);
	}
		else
		{
			cnf.getParentFile().mkdirs();
			cnf.createNewFile();
			System.out.println(cnf.getAbsolutePath());
			conf.setFile(cnf);
			//Make Configs
			conf.setProperty("Server name", "MCThunder Default Server Name");
			conf.setProperty("Port", 25565);
			conf.setProperty("World Name", "world");
			conf.setProperty("Online Mod", true);
			conf.setProperty("Server Owner Name", "Lukario45");
			
			
			conf.save();
		}
	}
}
