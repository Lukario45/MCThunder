package net.mcthunder.src;

import java.io.File;
import java.util.ArrayList;

/**
 * Groups.java
 * This is the Main class For the Player Groups
 * ~Lukario45
 * Author 3picKillz
 */
public class Groups 
{
	static ArrayList<Groups> groups = new ArrayList<Groups>();
	
	public String RankName;
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
		
	}
	
	public static void LoadMembers()
	{
		if(!new File("Ranks").exists());
		if(!new File("Ranks").mkdir());
		return;
	}
	
}
