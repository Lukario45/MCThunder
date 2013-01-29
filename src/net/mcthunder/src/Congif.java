package net.mcthunder.src;

import java.io.File;

import java.io.IOException;
import java.util.Random;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class Congif 
{
	private static PropertiesConfiguration conf;
	public static File cnf = new File(System.getProperty("user.home") + "/.MCThunder/config.yml");
	private static int port;
	private static String serverName;
	private static String worldName;
	private static boolean onlineMode;
	private static String ownerName;
	private static String globalChatNick;
	private static boolean netherWorld;
	private static boolean irc;
	private static String ircServer;
	private static int ircPort;
	private static String opChannel;
	private static String channelOne;
	private static String channelTwo;
	
	

	public static void loadConfig() throws ConfigurationException, IOException
	{
		Random r = new Random();
		int random = r.nextInt();
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
			conf.setProperty("Server_Name", "MCThunder Default Server Name");
			conf.setProperty("Port", 25565);
			conf.setProperty("World_Name", "world");
			conf.setProperty("Nether_World", true);
			conf.setProperty("Online_Mode", true);
			conf.setProperty("Server_Owner_Name", "Lukario45");
			conf.setProperty("Global_Chat_Nick", "MCT" + random);
			conf.setProperty("IRC", false);
			conf.setProperty("IRC_Server", "irc.mcthunder.net");
			conf.setProperty("IRC_Port", 6667);
			conf.setProperty("IRC_OP_Channel", "#mcthunderop");
			conf.setProperty("First_IRC_Channel", "#MCThunderIRC");
			conf.setProperty("Seccond_IRC_Channel", "#MCThunderIRC2");
			
			conf.save();
		}
		port = conf.getInt("Port");
		serverName = conf.getString("Server_Name");
		worldName = conf.getString("World_Name");
		netherWorld = conf.getBoolean("Nether_World");
		onlineMode = conf.getBoolean("Online_Mode");
		
	}
}
