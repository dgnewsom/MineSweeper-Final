package model;

 
/**
 * MineTileClass to store tile information for a tile in the game
 * 
 * @author Daniel Newsom
 * @version 3.0
 * 
 */
public class MineTile
{
	private boolean mined;
	private int minedNeighbours;
	private boolean revealed;
	private boolean marked;

	/**
	 * Constructor for the MineTile Class
	 */
	public MineTile()
	{
		mined = false;
		minedNeighbours = 0;
		revealed = false;
		marked = false;
	}

	/**
	 * Increment minedNeighbours
	 */
	public void addNeighbour() 
	{
		minedNeighbours ++;
	}
	/**
	 * Checks if the tile is correctly marked
	 * @return boolean true if both marked and mined
	 */
	public boolean isCorrect()
	{
		return marked && mined;
	}
	
	/*
	 * Getters and setters
	 */
	public boolean isMined()
	{
		return mined;
	}

	public void setMined(boolean mined)
	{
		this.mined = mined;
	}

	public int getMinedNeighbours()
	{
		return minedNeighbours;
	}

	public void setMinedNeighbours(int minedNeighbours)
	{
		this.minedNeighbours = minedNeighbours;
	}

	public boolean isRevealed()
	{
		return revealed;
	}

	public void setRevealed(boolean revealed)
	{
		this.revealed = revealed;
	}

	public boolean isMarked()
	{
		return marked;
	}

	public void setMarked(boolean marked)
	{
		this.marked = marked;
	}
	
	
}
