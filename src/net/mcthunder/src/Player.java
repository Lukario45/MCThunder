package net.mcthunder.src;
/**
 * Player.java 
 * This Class will be for The Players
 * ~Lukario45
 */

public class Player 
{
  private String PlayerNick;
	public Player(String pn)
	{
		this.PlayerNick = pn;
	}
	//Getters
	public String getPlayerNick()
	{
		return this.PlayerNick;
	}
	//To String
	public String toString()
	{
		return PlayerNick;
	}
}
