package net.mcthunder.src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.Random;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class Config 
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
	
	public Config() { 			//default constructor. make a config file for easy editing
		try {					//if we don't need to make a config file, at least we have a constructor now.
			FileWriter test = new FileWriter("config.txt"); //I'll use better names later.
			BufferedWriter test2 = new BufferedWriter(test); //
			test2.write(" "); //ill edit this later.
			test2.close();
			
		}catch (Exception e) {	//just in case.
			System.out.println("Error - " + e.getMessage());
		}
	}							//feel free to get rid of this. i'm not fully sure what to add to this project, so i just made this.

	

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
		ownerName = conf.getString("Server_Owner_Name");
		globalChatNick = conf.getString("Global_Chat_Nick");
		irc = conf.getBoolean("IRC");
		ircServer = conf.getString("IRC_Server");
		ircPort = conf.getInt("IRC_Port");
		opChannel = conf.getString("IRC_OP_Channel");
		channelOne = conf.getString("First_IRC_Channel");
		channelTwo = conf.getString("Seccond_IRC_Channel");	
	}
	//The Config Getters
	public static int getPort()
	{
		return port;
	}
	public static String getServerName()
	{
		return serverName;
	}
	public static String getWorldName()
	{
		return worldName;
	}
	public static boolean getNetherWorld()
	{
		return netherWorld;
	}
	public static boolean getOnlineMode()
	{
		return onlineMode;
	}
	public static String getOwnerName()
	{
		return ownerName;
	}
	public static String getGlobalChatNick()
	{
		return globalChatNick;
	}
	public static boolean getIrc()
	{
		return irc;
	}
	public static String getIrcServer()
	{
		return ircServer;
	}
	public static int getIrcPort()
	{
		return ircPort;
	}
	public static String getOpChannel()
	{
		return opChannel;
	}
	public static String getChannelOne()
	{
		return channelOne;
	}
	public static String getChannelTwo()
	{
		return channelTwo;
	}
	
	
	
}
