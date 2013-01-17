package net.mcthunder.src;
/**
 * Player.java 
 * This Class will be for The Players
 * ~Lukario45
 */

public class Player 
{

  public  String Username;
  private String PlayerNick;
  private Float PlayerEXP;
  private boolean OnGround;
  private String PlayerRank;
	public Player(String pn, Float EXP, boolean OG, String pr)
	{
		this.PlayerNick = pn;
		this.PlayerEXP = EXP;
		this.OnGround = OG;
		this.PlayerRank = pr;
	}
	//Getters
	//Get the Players Nick 
	public String getUsername()
	{
		return this.Username;
	}
	public String getPlayerNick()
	{
		return this.PlayerNick;
	}
	public Float getPlayerEXP()
	{
		return this.PlayerEXP;
	}
	public String getPlayerRank()
	{
		return this.PlayerNick;
	}
	public boolean getOnGround()
	{
		return OnGround;
		
	}
	//is the player flying or on the ground?
	 
	
}
