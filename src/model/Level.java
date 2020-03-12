package model;

import java.util.Optional;

import application.Main;
import application.MineSweeper;
import gui.Images;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Level Enum to represent and store the currently selected difficulty level
 * 
 * @author Daniel Newsom
 * @version 3.0
 *
 */
public enum Level {
	
	BEGINNER("Beginner"), 
	EASY("Easy"), 
	MEDIUM("Medium"), 
	HARD("Hard"), 
	CHALLENGING("Challenging"), 
	CRAZY("Crazy"), 
	CUSTOM("Custom");

	private String descriptionName;
	private static int[] custom = {10,10,10};
	private static Level level = Level.BEGINNER;
	
	/**
	 * Constructor for the Enum
	 * @param descriptionName representing name for toString()
	 */
	Level(String descriptionName) {
		this.descriptionName = descriptionName;
	}
	
	/**
	 * Returns int[] containing rows, cols and number of mines based on the level input
	 * @param level representing the current level
	 * @return int[] representing parameters for the game
	 */
	public static int[] getLevelArray(Level level) 
	{
		int rows = 0;
		int cols = 0;
		int mines = 0;
		int[] levelOutput = new int[3];
		
		switch (level) {
		case BEGINNER:
			rows = 10;
			cols = 10;
			mines = 10;
			break;
		case EASY:
			rows = 10;
			cols = 10;
			mines = 15;
			break;
		case MEDIUM:
			rows = 20;
			cols = 20;
			mines = 40;
			break;
		case HARD:
			rows = 20;
			cols = 20;
			mines = 60;
			break;
		case CHALLENGING:
			rows = 30;
			cols = 30;
			mines = 200;
			break;
		case CRAZY:
			rows = 30;
			cols = 30;
			mines = 400;
			break;
		case CUSTOM:
			rows = custom[0];
			cols = custom[1];
			mines = custom[2];
			break;
		default:
			break;
		}
		
		levelOutput[0] = rows;
		levelOutput[1] = cols;
		levelOutput[2] = mines;
		
		return levelOutput;
	}
	
	/**
	 * Method to get input from the user to enable custom game parameters
	 */
	public static void setCustomLevel() {	

		/*
		 * Create observable list from 1 - 200 for the spinners on the dialog
		 */
		Integer[] range = new Integer[200];
		for (int i = 0; i < range.length; i++) {
			range[i] = i+1;
		}
		ObservableList<Integer> rangeList = FXCollections.observableArrayList(range);
		
			/*
			 * Create and initialise the popup window
			 */
	        final Stage dialog = new Stage();
	        BorderPane rootPane = new BorderPane();
	        dialog.setTitle("Custom Level");
	        dialog.initModality(Modality.APPLICATION_MODAL);
	        dialog.initOwner(Main.getStage());
	        dialog.getIcons().add(new Image(Images.ICON.getImage()));
	        dialog.setHeight(220);
	        dialog.setWidth(480);
	        dialog.setResizable(false);
	        
			/*
			 * Create and initialise title pane for the popup        
			 */
	        TilePane titlePane = new TilePane();
	        titlePane.setAlignment(Pos.CENTER);
	        Label titleLabel = new Label("Please choose your custom settings");
	        titleLabel.setFont(new Font(18));
	        titleLabel.setTextAlignment(TextAlignment.CENTER);
	        titleLabel.setPadding(new Insets(15,0,15,0));
	        titlePane.getChildren().add(titleLabel);
	        
	        /*
	         * Create panes for the three inputs and labels 
	         */
	        HBox inputs = new HBox();
	        inputs.setAlignment(Pos.CENTER);
	        inputs.setPadding(new Insets(20));
	        inputs.setSpacing(20);
	        HBox rowsPane = new HBox();
	        rowsPane.setSpacing(10);
	        HBox columnsPane = new HBox();
	        columnsPane.setSpacing(10);
	        HBox minesPane = new HBox();
	        minesPane.setSpacing(10);
	        
	        /*
	         * Create pane for the buttons 
	         */
	        TilePane buttons = new TilePane();
	        buttons.setPadding(new Insets(20));
	        buttons.setHgap(10);
	        BorderPane buttonsPane = new BorderPane();
	        
	        /*
	         * Construct panes for each of the inputs containing
	         * a label and a spinner
	         */
	        Label rowsLabel = new Label("Rows: ");
	        rowsLabel.setFont(new Font(15));
	        ComboBox<Integer> rowComboBox = new ComboBox<Integer>(rangeList);
	        rowComboBox.setValue(custom[0]);
	        rowsPane.getChildren().addAll(rowsLabel, rowComboBox);
	        
	        Label columnsLabel = new Label("Columns: ");
	        columnsLabel.setFont(new Font(15));
	        ComboBox<Integer> columnComboBox = new ComboBox<Integer>(rangeList);
	        columnComboBox.setValue(custom[1]);
	        columnsPane.getChildren().addAll(columnsLabel, columnComboBox);
	        
	        Label minesLabel = new Label("Mines: ");
	        minesLabel.setFont(new Font(15));
	        ComboBox<Integer> mineComboBox = new ComboBox<Integer>(rangeList);
	        mineComboBox.setValue(custom[2]);
	        minesPane.getChildren().addAll(minesLabel, mineComboBox);
	        
	        //Add three input panes to the main inputs pane
	        inputs.getChildren().addAll(rowsPane, columnsPane, minesPane);
	        
	        /*
	         * Create the buttons for applying the settings or cancelling
	         * and set event handlers
	         */
	        Button okButton = new Button("OK");
	        okButton.setMinWidth(75);
	        //Set event to set custom parameters close window and start new game
	        okButton.setOnMouseClicked(e->{	custom[0] = rowComboBox.getValue();
											custom[1] = columnComboBox.getValue();
											custom[2] = mineComboBox.getValue();
											dialog.close();
											Main.startGame(Main.getStage());});

	        Button cancelButton = new Button("Cancel");
	        cancelButton.setMinWidth(75);
	        cancelButton.setOnMouseClicked(e->{dialog.close();});
	        
	        //add buttons to buttons pane
	        buttons.getChildren().addAll(okButton, cancelButton);
	        buttonsPane.setRight(buttons);;
	        
	        //Add three main panes to root pane
	        rootPane.setTop(titlePane);
	        rootPane.setLeft(inputs);
	        rootPane.setBottom(buttonsPane);
	        
	        //create and set scene and add to main window
	        Scene dialogScene = new Scene(rootPane, 500, 40);
	        dialog.setScene(dialogScene);
	        dialog.show();
		
		
	}

	/**
	 * Method to change the level when new level is selected from the menu 
	 * @param e triggering event
	 * @param isPlaying shows whether the current game is still playing or finished
	 */
	public static void changeLevel(ActionEvent e, boolean isPlaying) {
		if(isPlaying) {
			
			/*
			 * If still playing show confirmation before changing level and starting new game
			 * if not set new level and start new game without confirmation
			 */ 
			Alert alert;
			Optional<ButtonType> result;
			MineSweeper.pauseTimer();
			
			alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.getStage());
			alert.setTitle("Confirm");
			alert.setHeaderText("Changing level will start new game.");
			alert.setContentText("Are you sure?");

			result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				RadioMenuItem menuItem = (RadioMenuItem) e.getSource();
				if(menuItem.getUserData() == Level.CUSTOM) {Level.setCustomLevel();}
				level = ((Level) menuItem.getUserData());
				Main.startGame(Main.getStage());
			}else {
				alert.close();
			}
			MineSweeper.playTimer();
		}
		else {
			RadioMenuItem menuItem = (RadioMenuItem) e.getSource();
			level = ((Level) menuItem.getUserData());
			Main.startGame(Main.getStage());
		}
		
	}
	
	/**
	 * tostring method to return the name along with the number of rows, columns and mines
	 */
	public String toString() {
		
		return getDescriptionName() + "  " + getLevelParameters();
	}

	public String getDescriptionName() {
		return descriptionName;
	}
	
	public String getLevelParameters() {
		int[] outputs = getLevelArray(this);
		String rows;
		String cols;
		String mines;
		
		if(outputs[0] > 1) {
			rows = String.format("%d Rows", outputs[0]);
		}
		else {
			rows = "1 row";
		}
		if(outputs[1] > 1) {
			cols = String.format("%d Columns", outputs[1]);
		}
		else {
			cols = "1 Column";
		}
		if(outputs[2] > 1) {
			mines = String.format("%d Mines", outputs[2]);
		}
		else {
			mines = "1 Mine";
		}
			
		
		return String.format("(%s, %s, %s)", rows, cols, mines);
	}
	/*
	 * Getters and setters
	 */
	public static Level getLevel() {return level;}
	
	public static void setLevel(String levelDescription) {
		for(Level level : Level.values()) {
			if(levelDescription.equals(level.descriptionName)) {
				Level.level = level;
			}
		}
	}
	
	public static void setCustomArray(int[] customInput) {
		Level.custom = customInput;
	}
	
	/*
	 * Return an index based upon level input 
	 * used to reference position in highscores list
	 */
	public static int getLevelIndex(Level level) {

		switch (level) {
		case BEGINNER:
			return 0;
		case EASY:
			return 1;
		case MEDIUM:
			return 2;
		case HARD:
			return 3;
		case CHALLENGING:
			return 4;
		case CRAZY:
			return 5;
		case CUSTOM:
			return 6;
		default:
			return 0;
		}
	}

	/*
	 * returns a KeyCode value to assign to the menu shortcut
	 */
	public static KeyCode getKeyCode(Level level) {
		switch (level) {
		case BEGINNER:
			return KeyCode.DIGIT1;
		case EASY:
			return KeyCode.DIGIT2;
		case MEDIUM:
			return KeyCode.DIGIT3;
		case HARD:
			return KeyCode.DIGIT4;
		case CHALLENGING:
			return KeyCode.DIGIT5;
		case CRAZY:
			return KeyCode.DIGIT6;
		case CUSTOM:
			return KeyCode.C;
		default:
			return null;
		}
	}
}
