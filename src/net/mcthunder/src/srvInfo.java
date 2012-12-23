package net.mcthunder.src;

import java.util.ArrayList;
import java.util.List;

/**
 * srvInfo.java
 * This Class if for all of the Server Information Like Versions and Devs
 * Its Like the AsseblyInfo.cs for c# Servers
 * ~Lukario45
 */

public class srvInfo 
{
	private static String srvVersion = "1.0.0";
	public static String mcVersion = "1.4.6";
	private static List<String> devs = new ArrayList<String>();
	public static String getVersion(){
		return srvVersion;
	}
	public static List<String> getDevelopers(){
		devs.add("3pic_Killz");
		devs.add("KillerFoxE");
		devs.add("Legorek");
		devs.add("Lukario45");
		devs.add("MineDroidFTW");
		devs.add("zack6849");
		return devs;
	}

}
