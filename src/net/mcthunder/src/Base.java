package net.mcthunder.src;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Base.java
 * This class is MCThunders Base Class
 * ~Lukario45
 */


/**
 * MCThunder SMP
 * Developers Lukario45, Brandon Busk, Zack Craig, 3pik_killz, and Rekkeh.
 * For Minecraft 1.4.5
 *
 */

public class Base 

{
	//Variables that are needed
	static String curVersion;
	
	
	//send console message;
	public static String tellConsole(String conMSG)
	{
		System.out.println(conMSG);
		return null;
		
	}
	
	
	//This is for getting the version off of MCThunder.net
	public static String getCurVersion(String curVersion)
	{
		try {
		    	URL url = new URL("http://www.mcthunder.net/version.html");
		    	BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		        //String str; 
		    	curVersion = in.readLine();
		    	in.close();
		    	return curVersion;	
		    	
			} 
		catch (MalformedURLException e) 
		{
			
		} 
		catch (IOException e) 
		{
			
		}
		return curVersion;
 
		}
	//This is for seeing if the Server Version and most Current Version are the same
	public static String chkVersion()
	{
		if (  curVersion.equals(srvInfo.srvVersion))
		{
			tellConsole("Your version of MCThunder is up to date!");
			return null;
		
		}
		
		else 
		{
			tellConsole("MCThunder is out of date! You should consider updating to version " + curVersion + "!");
			return null;
		}
		
		
	}
	
	
	
	
	
	/**
	 * Main 
	 * @return 
	 */
	
	
	
	public static void main(String[] args) 
	{
		//Before Startup Messages
		tellConsole("MCThunder " + srvInfo.srvVersion + " for Minecraft " + srvInfo.mcVersion );
		tellConsole("Developers");
		tellConsole(srvInfo.dev3pic);
		tellConsole(srvInfo.devLego);
		tellConsole(srvInfo.devLuka);
		tellConsole(srvInfo.devMine);
		tellConsole(srvInfo.devZack);
		tellConsole(srvInfo.devKiller);
		start();
		
		
		
		
		
	}
	
	/**  
	 * Start The Server
	 */
	public static void start()
	{
		tellConsole("Server startup initiated...");
		tellConsole("Checking Version...");
		curVersion = getCurVersion(curVersion);
		chkVersion();
		
		
		
		
		
		
	}
	
	
}
