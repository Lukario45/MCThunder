package net.mcthunder.src;
/**
 * Player.java 
 * This Class will be for The Players
 * ~Lukario45
 */

public class Player 
{
  private String PlayerNick;
  private Float PlayerEXP;
  private boolean OnGround;
	public Player(String pn, Float EXP, boolean OG)
	{
		this.PlayerNick = pn;
		this.PlayerEXP = EXP;
		this.OnGround = OG;
	}
	//Getters
	//Get the Players Nick 
	public String getPlayerNick()
	{
		return this.PlayerNick;
	}
	public Float getPlayerEXP()
	{
		return this.PlayerEXP;
	}
	public boolean getOnGround()
	{
		if (OnGround == true)
		{
			Base.tellConsole("INFO", PlayerNick + " Is Not Flying");
			return true;
		}
		else
		{
			Base.tellConsole("INFO",PlayerNick + " Is not on the ground");
			return false;
		}
		
	}
	//is the player flying or on the ground?
	 
	
}
