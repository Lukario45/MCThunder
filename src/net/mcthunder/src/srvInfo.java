package net.mcthunder.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * srvInfo.java
 * This Class if for all of the Server Information Like Versions and Devs
 * Its Like the AsseblyInfo.cs for c# Servers
 * ~Lukario45
 */

public class srvInfo 
{
	private static String srvVersion = "1.0.0";
	public static String mcVersion = "1.4.7";
	public static String serverName = "MCThunder Premium";
	public static int port = 25565;
	private static String[] devs = new String[] 
	{
		"3pic_Killz", "Legorek", "Lukario45", "MineDroidFTW", "zack6849", "Mod_Chris"
	};
	public static String getVersion()
	{
		return srvVersion;
	}
	public static String getServerName()
	{
		return ServerName; 
	}
	public static List<String> getDevelopers()
	{
		return Collections.unmodifiableList(Arrays.asList(devs));
	}
	
	/**public static void LoadProperties()
	{
		
	}
	*/

}
