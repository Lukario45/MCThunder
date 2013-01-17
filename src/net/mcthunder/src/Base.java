package net.mcthunder.src;

import java.io.BufferedReader;
import net.mcthunder.src.Groups;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Base.java
 * This class is MCThunders Base Class
 * ~Lukario45
 */

/**
 * MCThunder SMP Developers Lukario45, Brandon Busk, Zack Craig, 3pik_killz, and
 * Rekkeh. For Minecraft 1.4.6
 * 
 */

public class Base 
{
	static String curVersion;
	public static void main(String[] args) 
	{
		// Before Startup Messages
		tellConsole("INFO","MCThunder " + srvInfo.getVersion() + " for Minecraft "+ srvInfo.mcVersion);
		tellConsole("INFO", "Developers");
		for(String s : srvInfo.getDevelopers())
		{
			tellConsole("INFO", s);
		}
		start();
	}
	//Test
//test pull
	/**
	 * Start The Server
	 */
	public static void start() 
	{
		
		tellConsole("INFO", "Server startup initiated...");
		tellConsole("INFO","NULL Commands, NULL Acheivementsm, NULL Blocks");
		tellConsole("INFO", "Checking Version...");
		curVersion = getCurVersion(curVersion);
		chkVersion();
		tellConsole("INFO", "Loading Ranks...");
		Groups.LoadMembers();
		tellConsole("INFO", "Checking for Plugins...");
		tellConsole("INFO", "Checking for Worlds...");
		
	
	}
	/**
	 * @param prefix: the string to prefix the message with
	 * @param message: the message to print to the log
	 */
	public static void tellConsole(String prefix, String message)
	{
		String time = String.format("[%tF %<tT]", new Date());
		System.out.println(time + " [" + prefix + "] " + message);
	}
	
	// This is for getting the version off of MCThunder.net
	public static String getCurVersion(String curVersion) 
	{
		try 
		{
			URL url = new URL("http://www.mcthunder.net/version.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(
			url.openStream()));
			curVersion = in.readLine();
			in.close();
		} 
		catch (MalformedURLException e) 
		{
			tellConsole("SEVERE", "Error checking version, malformed url!");
		} 
		catch (IOException e) 
		{
			tellConsole("SEVERE", "Error checking current version, IOExcpetion");
		}
		return curVersion;
	}

	// This is for seeing if the Server Version and most Current Version are the
	// same
	public static void chkVersion() 
	{
		if (curVersion.equals(srvInfo.getVersion())) 
		{
			tellConsole("INFO","Your version of MCThunder is up to date!");
		}
		else 
		{
			tellConsole("WARNING", "MCThunder is out of date! You should consider updating to version " + curVersion + "!");
		}
	}

}
