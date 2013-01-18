package net.mcthunder.src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Groups.java
 * This is the Main class For the Player Groups
 * ~Lukario45
 * Author 3picKillz
 */
public class Groups 
{
	static ArrayList<Groups> groups = new ArrayList<Groups>();
	private ArrayList<String> members = new ArrayList<String>();
	
	public static String RankName;
	public int permissionlvl;
	public String Color;
	public boolean isOP = false;
	
	public Groups(String RankName, int permissionlvl, String Color)
	{
		this.RankName = RankName;
		this.permissionlvl = permissionlvl;
		this.Color = Color;
	}
	
	public void groups()
	{
		this.LoadRanks();
	}
	
	void LoadRanks()
	{
		Logger.getLogger("Loading Ranks");	
		if(!new File("Ranks").exists());
		if(!new File("Ranks").mkdir());
		return;
	}
	
	public static void LoadMembers() throws IOException
	{
		if (!new File("ranks/" + RankName).exists());
		return;
	}
	
	public void addMember(String name)
	{
		if (members.contains(name))
			return;
		members.add(name);
		saveMembers();
	}
	
	public void addPlayer(Player p){
		addMember(p.Username);
		
	}
	
	public void saveMembers(){
		
	}
	
}
