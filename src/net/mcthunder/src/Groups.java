package net.mcthunder.src;
/**
 * Groups.java
 * This is the Main class For the Player Groups
 * ~Lukario45
 * Author 3picKillz
 */
public class Groups 
{
	
	public String RankName;
	public int permissionlvl;
	public String Color;
	
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
	
	public void addRank(String RankName, int permissionlvl, String Color)
	{
		
	}
	
}
