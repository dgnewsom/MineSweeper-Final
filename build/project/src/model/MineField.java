package model;
 
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * MineField Class to setup the minefield.
 * 
 * @author Daniel Newsom
 * @version 3.0
 * 
 */
public class MineField
{
	private MineTile[][] minefield;
	private int numberOfMines;
	private int minesPlaced = 0;
	private int rows;
	private int cols;
	private int totalTiles;
	private StringProperty tilesFlagged;
	private StringProperty minesRemaining;
	
	/**
	 * Constructor for the Minefield Class
	 * @param inputs int[] representing number of rows, columns and mines
	 */
	public MineField(int[] inputs) {
		this.rows = inputs[0];
		this.cols = inputs[1];
		totalTiles = rows * cols;
		// create arrays
		if (rows <= 0 || cols <= 0)
		{
			minefield = new MineTile[0][0];
		}
		else
		{
			minefield = new MineTile[rows][cols];
		}
		// initialise arrays
		fillArrays();
		// if number of mines is negative set to 0
		if (inputs[2] <= 0)
		{
			this.numberOfMines = 0;
		}
		else
		{
			this.numberOfMines = inputs[2];
		}
		
		tilesFlagged = new SimpleStringProperty(String.format("%3d", 0));
		minesRemaining = new SimpleStringProperty(String.format("%3d", numberOfMines));
	}

	/**
	 * Method to try and place a mine on a tile and increment surrounding tiles
	 * numbers
	 * 
	 * @param row represents the number of the row
	 * @param col represents the number of the column
	 * @return Boolean true if successfully mined, false if not.
	 */
	public boolean mineTile(int row, int col)
	{
		 //Check that input is not out of bounds
        if (row < 0 || col < 0 || row+1 > rows || col+1 > cols || (col == 0 && row == 0)) {
            return false;
        }
        //Check that the tile is not already mined and that there are mines left to place
		if (this.minefield[row][col].isMined() || minesPlaced >= numberOfMines)
		{
			return false;
		}
		else
		{
			//mine the tile
			minefield[row][col].setMined(true);
			//Increment the surrounding neighbours whilst checking for out of bounds
			for (int r = row - 1; r <= row + 1; r++)
			{
				if (!(r < 0) && !(r >= rows))
				{
					for (int c = col - 1; c <= col + 1; c++)
					{
						if (!(c < 0) && !(c >= cols))
						{
							minefield[r][c].setMinedNeighbours(minefield[r][c].getMinedNeighbours() + 1);
						}
					}
				}
			}
			// increase number of mines placed
			minesPlaced++;
			return true;
		}
	}

	/**
     * method to continuously mine tiles until no mines left to place.
     */
    public void populate()
    {
        /*
         * Check that there is sufficient space for the mines
         * if not change number of mines to the maximum possible.
         */
        if (numberOfMines > totalTiles -1) {
            numberOfMines = totalTiles -1;
        }

        //select a random row and column and try to mine it
        Random rand = new Random();
        int row;
        int col;
        while (getNumMinesRemaining() > 0)
        {
            row = rand.nextInt(rows);
            col = rand.nextInt(cols);

            mineTile(row, col);
        }
    }

	/**
	 * Method to initialise the array with blank data.
	 */
	private void fillArrays()
	{
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < minefield[row].length; col++)
			{
				minefield[row][col] = new MineTile();
			}
		}
	}

	/**
	 * Method to toggle whether a tile is marked.
	 * @param row int representing the row to mark.
	 * @param col int representing the col to mark.
	 */
	public void markTile(int row, int col)
	{
		
		MineTile tile = minefield[row][col];
		//if tile is already revealed do nothing
		if(tile.isRevealed()) {
			
		}
		
		//If not Check tile and if marked unmark. if unmarked then mark
		else if (tile.isMarked())
		{
			tile.setMarked(false);
		}
		else
		{
			tile.setMarked(true);
		}
	}

	/**
	 * Method to step on a tile and return false if mined
	 * otherwise return true and check its neighbours recursively
	 * to reveal all tiles with no mined neighbours
	 * @param row int representing the row to step on
	 * @param col int representing the col to step on
	 * @return boolean false if mined true if not
	 */
	public boolean stepOnTile(int row, int col)
	{
		
		
		MineTile tile = minefield[row][col];
		//if tile is marked return true
		if(tile.isMarked()){
			return true;
		}
		//If tile is mined return false
		if (tile.isMined())
		{
			return false;
		}
		else
		{
			//If tile is revealed return true
			if (tile.isRevealed())
			{
				return true;
			}
			
			/*
			 * If not, set revealed and then recursively
			 * reveal all neighbours with no mined neighbours. 
			 */
			tile.setRevealed(true);
			if (tile.getMinedNeighbours() == 0)
			{
				for (int r = row - 1; r <= row + 1; r++)
				{
					if (!(r < 0) && !(r >= rows))
					{
						for (int c = col - 1; c <= col + 1; c++)
						{
							if (!(c < 0) && !(c >= cols))
							{
								stepOnTile(r, c);
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Method to check if all mineTiles that are mined 
	 * are also marked or if only mines remain unrevealed. 
	 * Used to check for end of the game.
	 * @return boolean true is all mines marked correctly false if not
	 */
	
	public boolean areAllMinesRevealed()
	{
		//initialise counter
		int correctTiles = 0;
		int tilesMarked = 0;
		int tilesRevealed = 0;
		
		//check each tile and increase count if it is both mined and marked 
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				if (minefield[row][col].isCorrect())
				{
					correctTiles ++;
				}
				if (minefield[row][col].isMarked()) 
				{
					tilesMarked ++;
				}
				if (minefield[row][col].isRevealed())
				{
					tilesRevealed++;
				}
			}
		}
		
		tilesFlagged.set(String.format("%3d", tilesMarked));
		minesRemaining.set(String.format("%3d", numberOfMines - tilesMarked));
		
		//return if correct count matches number of mines placed 
		return ((minesPlaced == correctTiles)&&(tilesMarked == correctTiles)||totalTiles - tilesRevealed == minesPlaced);
	}

	/**
	 * Method to reveal all the mines,
	 * used at the end of the game
	 */
	public void revealAllMines()
	{
		//Check each tile and if mined set to revealed
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				if (minefield[row][col].isMined())
				{
					minefield[row][col].setRevealed(true);
				}
			}
		}
	}

	/*
	 * Getter Methods
	 */
	public MineTile getMineTile(int row, int col)
	{
		return minefield[row][col];
	}
	
	public int getNumberOfMines()
	{
		return numberOfMines;
	}

	public int getNumMinesRemaining()
	{
		return numberOfMines - minesPlaced;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public int getTotalTiles() {
		return totalTiles;
	}
	
	public StringProperty getTilesFlagged() {
		return tilesFlagged;
	}
	
	public StringProperty getMinesRemaining() {
		return minesRemaining;
	}
	
	public MineTile[][] getMinefield() {
		return minefield;
	}
}
