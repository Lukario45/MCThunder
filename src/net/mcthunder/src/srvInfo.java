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
	private static String devLego = "Legorek";
	private static String devLuka = "Lukario45";
	private static String devMine = "MineDroidFTW";
	private static String devZack = "zack6849";
	private static String dev3pic = "3pic_Killz";
	private static String devKiller = "KillerFoxE";
	
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
