package gui;

import java.util.Optional;

import application.Main;
import application.MineSweeper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.HighScores;
import model.Level;
import model.MineTile;

/**
 * TileButton Class to represent a mineTile on the GUI
 * @author Daniel Newsom
 * @version 3.0
 */
public class TileButton extends Button {

	private int row;
	private int col;
	private MineTile tile;
	private MineSweeper game;

	/**
	 * Constructor for the tileButton class
	 * @param row currently allocated row
	 * @param col currently allocated column
	 * @param tile currently allocated mineTile
	 * @param game represents the current game
	 */
	public TileButton(int row, int col, MineTile tile, MineSweeper game) {
		super();
		this.row = row;
		this.col = col;
		this.tile = tile;
		this.game = game;
	}

	/**
	 * Sets the objects graphic based upon its current mineTiles state
	 */
	public void setButtonContent() {

		if(tile.isMarked()) {
			setGraphic(new ImageView(Images.FLAG.getImage()));
		}
		else {
			setGraphic(null);
		}
		if(tile.isRevealed()) {

			if(tile.isMined()) {
				setGraphic(new ImageView(Images.MINE.getImage()));
			}
			else if(tile.getMinedNeighbours() == 0) {
				setDisable(true);
			}
			else {
				this.setText("" + tile.getMinedNeighbours());
				this.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 15));
				
				Color textColour = getTextColour();
				
				this.setTextFill(textColour);
			}
		}
	}
	
	
	/*
	 * Return colour based on int from mined neighbours 
	 */
	private Color getTextColour() {
		switch (tile.getMinedNeighbours()) {
		case 1:
			return Color.BLUE;
		case 2:
			return Color.GREEN;
		case 3:
			return Color.RED;
		case 4:
			return Color.PURPLE;
		case 5:
			return Color.MAROON;
		case 6:
			return Color.TURQUOISE;
		case 7:
			return Color.BLACK;
		case 8:
			return Color.GRAY;
		default:
			return Color.BLACK;
		}
	}
	
	/*
	 * method to handle clicking on the tileButton
	 */
	public void clickOnTile(MouseEvent event) {
		boolean lost = false;
		if(MineSweeper.isPlaying()) {
			Alert alert;
			Optional<ButtonType> result;
			TileButton tile = (TileButton)event.getSource();
			//if left button clicked step on corresponding mineTile
			if(event.getButton() == MouseButton.PRIMARY) {
				if(!game.getMinefield().stepOnTile(tile.getRow(), tile.getColumn())) {
					/*
					 * If tile is a mine. 
					 * Stop game, reveal all mines, refresh display
					 * and display you lost dialog - ask to start a new game
					 */
					MineSweeper.stopTimer();
					game.getMinefield().revealAllMines();
					game.setPlaying(false);
					lost = true;
					game.getGui().refreshGameField();
					alert = new Alert(AlertType.CONFIRMATION);
					alert.initOwner(Main.getStage());
					alert.setTitle("BANG");
					alert.setHeaderText("Sorry you lost!\n"
							+ Level.getLevel());
					alert.setContentText("Start new Game?");

					result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						Main.startGame(Main.getStage());
					}
				}
			}
			//if right button clicked mark tile
			if(event.getButton() == MouseButton.SECONDARY) {game.getMinefield().markTile(tile.getRow(), tile.getColumn());}
			
			/*
			 * If all mines are revealed. 
			 * Stop game, reveal all mines, refresh display
			 * and display you won dialog - ask to start a new game
			 *
			 */
			if(game.getMinefield().areAllMinesRevealed() && !lost) {
				game.getMinefield().revealAllMines();
				game.setPlaying(false);
				MineSweeper.stopTimer();
				game.getGui().refreshGameField();
				
				HighScores.addScore(Level.getLevel(), game.getTime());
				
				MineSweeper.stopTimer();
				
				alert = new Alert(AlertType.CONFIRMATION);
				alert.initOwner(Main.getStage());
				alert.setTitle("Congratulations!");
				alert.setHeaderText("Well done you have won!\n"
									+ Level.getLevel()
									+"\nTime taken was " + MineSweeper.getTimeAsString(game.getTime()));
				alert.setContentText("Start new ?");
				result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					Main.startGame(Main.getStage());
				}
			}
			//refresh display on each click
			game.getGui().refreshGameField();
			
		}
	}
	
	/*
	 * Getters and setters
	 */
	public int getRow() {
		return row;
	}

	public int getColumn() {
		return col;
	}
}
