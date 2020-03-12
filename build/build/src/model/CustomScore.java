package model;

/**
 * CustomScore Class to represent a score for the custom level of the minesweeper game
 * @author Daniel Newsom
 * @version 3.0
 */
public class CustomScore extends Score {

	private int[] levelDetails;
	
	/**
	 * Constructor for the CustomScore class
	 * @param name String representing the scorers name
	 * @param time int representing the scorers time
	 * @param levelDetails int[] representing the number of rows, columns and mines used when score set.
	 */
	public CustomScore(String name, int time, int[] levelDetails ) {
		super(name, time);
		this.levelDetails = levelDetails; 
	}

	/**
	 * Create a string representation of the level details
	 * @return String representing the level details
	 */
	public String getlevelDetailsString() {
		String rows;
		String cols;
		String mines;
		
		if(levelDetails[0] > 1) {
			rows = String.format("%d Rows", levelDetails[0]);
		}
		else {
			rows = "1 row";
		}
		if(levelDetails[1] > 1) {
			cols = String.format("%d Columns", levelDetails[1]);
		}
		else {
			cols = "1 Column";
		}
		if(levelDetails[2] > 1) {
			mines = String.format("%d Mines", levelDetails[2]);
		}
		else {
			mines = "1 Mine";
		}
			
		return String.format("(%s, %s, %s)", rows, cols, mines);
	}
	/*
	 * Getters and Setters
	 */
	public int[] getLevelDetails() {
		return levelDetails;
	}
	
	public String toString() {
		return super.toString() + " " + getlevelDetailsString();
	}
}
