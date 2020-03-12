package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import application.Main;
import application.MineSweeper;
import gui.Images;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * Abstract HighScores Class to hold and process high scores for the minesweeper game
 * @author Daniel Newsom
 * @version 3.0
 *
 */
public abstract class HighScores implements Comparator<Score> {

	private static List<ArrayList<Score>> highScores;
	private static final File settingsFile = new File("settings.txt");
//	private static final File resetFile = new File("reset.txt");

	/**
	 * Import highscores and settings from file and populate highscores lists
	 * as well as set level and custom parameters from file
	 */
	public static void importSettingsFromFile() {

		//Initialise lists
		highScores = new ArrayList<ArrayList<Score>>() ;
		for (int i = 0; i < 7; i++) {
			highScores.add(new ArrayList<Score>());
		}
		try  
		{  
			/*
			 * Try to read from text file and populate highscore lists 
			 */
			FileReader fr = new FileReader(settingsFile);
			BufferedReader br=new BufferedReader(fr);
			ArrayList<Score> currentList = new ArrayList<Score>();

			for (int i = 0; i < highScores.size(); i++) {
				currentList = highScores.get(i);
				/*
				 * If importing custom levels populate list 
				 * with CustomScore objects
				 */
				if(i == 6) {
					for (int j = 0; j < 5; j++) {
						String[] score = br.readLine().split(":");
						String[] details = score[2].split(",");
						int rows = Integer.parseInt(details[0]);
						int cols = Integer.parseInt(details[1]);
						int mines = Integer.parseInt(details[2]);

						currentList.add(new CustomScore(score[0], Integer.parseInt(score[1]),new int[] {rows,cols,mines}));
					}
				}
				/*
				 * If not importing custom level populate list with Score objects
				 */
				else {
					for (int j = 0; j < 5; j++) {
						String[] score = br.readLine().split(":");
						currentList.add(new Score(score[0], Integer.parseInt(score[1])));
					}

				} 
			}
			/**
			 * Read and set the current level and custom parameters from the file
			 */
			Level.setLevel(br.readLine());
			int[] custom = new int[3];
			String[] customString = br.readLine().split(",");
			for (int i = 0; i < customString.length; i++) {
				custom[i] = Integer.parseInt(customString[i]);
			}
			Level.setCustomArray(custom);
			br.close();
		}  
		/**
		 * If file is not valid use reset file.
		 */
		catch(IOException e)  
		{  
			resetSettings();
		}
		catch(NullPointerException e) {
			resetSettings();
		}
		catch(ArrayIndexOutOfBoundsException e) {
			resetSettings();
		}

	}  

	/**
	 * Writes all highscores and settings to a text file
	 */
	public static void exportSettingsToFile() {
		try  
		{  
			FileOutputStream fos = new FileOutputStream(settingsFile);   
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));  

			for (int i = 0; i < highScores.size(); i++) {
				ArrayList<Score> currentList = highScores.get(i);
				if(i == 6) {
					for(int j = 0; j < currentList.size(); j++){
						CustomScore currentItem = (CustomScore) currentList.get(j);
						int[] levelDetails = currentItem.getLevelDetails();
						bw.write(currentItem.getName() + ":" + currentItem.getTime() + ":"
								+ levelDetails[0] + "," + levelDetails[1] + "," + levelDetails[2]);
						bw.newLine();
					}
				}
				else {
					for(int j = 0; j < currentList.size(); j++){
						Score currentItem = currentList.get(j);
						bw.write(currentItem.getName() + ":" + currentItem.getTime());
						bw.newLine();
					}
				}
			}
			bw.write(Level.getLevel().getDescriptionName());
			bw.newLine();
			int[] custom = Level.getLevelArray(Level.CUSTOM);
			bw.write(String.format("%d,%d,%d", custom[0], custom[1], custom[2]));
			bw.close();
		}
		catch(IOException e)  
		{  
			e.printStackTrace();  
		}
	}

	/*
	 * return the last score from the highscores for current level
	 */
	private static int getLastScore(Level level) {
		return highScores.get(Level.getLevelIndex(level)).get(4).getTime();
	}

	/**
	 * Add a score to high scores
	 * @param level level to add score to
	 * @param time int representing time to add
	 * @return boolean true if highscore added, false otherwise
	 */
	public static boolean addScore(Level level, int time) {
		boolean added = false;
		String name = "Player";
		/*
		 * If time is < the last entry on the highscores list for level
		 */
		if(time < getLastScore(level)) {

			/*
			 * Display dialog requesting name
			 */
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("New high score!");
			dialog.setHeaderText("Congratulations you have set a high score!");
			dialog.setContentText("Please enter your name:\n(Max 25 characters)");
			dialog.initOwner(Main.getStage());
			Optional<String> result = dialog.showAndWait();
			/*
			 * Check and adjust name input as required
			 */
			if (result.isPresent() && result.get().length() <= 25){
				name = result.get();
				if(name.length() == 0) {
					name = "Player";
				}
			}
			else {
				try {
					name = result.get();
				} catch (NoSuchElementException e) {} // if Cancel clicked leave name as player
				if(name.length() >= 25) {
					name = name.substring(0,25);
				}
			}
		}
		/*
		 * Add Score to the corresponding list by adding,
		 * then sorting the list, then removing last item
		 */
		ArrayList<Score> levelList = highScores.get(Level.getLevelIndex(level));
		Score score;
		if(level.equals(Level.CUSTOM)) {
			score = new CustomScore(name, time, Level.getLevelArray(level));
			levelList.add(score);
			score.sortList(levelList);
			levelList.remove(5);
			added = true;
		}
		else {
			score = new Score(name, time);
			levelList.add(score);
			score.sortList(levelList);
			levelList.remove(5);
			added = true;
		}
		displayLevelScores(Level.getLevel());
		return added;
	}


	/**
	 * Create and display popup window showing
	 * high scores for the given level
	 * @param level level to show scores for
	 */
	public static void displayLevelScores(Level level) {
		MineSweeper.pauseTimer();

		final Stage dialog = new Stage();

		dialog.setTitle("High Scores");
		dialog.getIcons().add(new Image(Images.ICON.getImage()));
		dialog.setResizable(false);

		BorderPane rootPane = new BorderPane();
		VBox mainPane = createScorePane(level, 25, 25);
		BorderPane buttonPane = new BorderPane();
		buttonPane.setPadding(new Insets(20));


		Button okButton = new Button("OK");
		okButton.setOnMouseClicked(e->{dialog.close();});
		okButton.setMinWidth(75);
		okButton.setFont(Font.font(15));
		buttonPane.setRight(okButton);

		rootPane.setCenter(mainPane);
		rootPane.setBottom(buttonPane);

		Scene dialogScene = new Scene(rootPane);
		dialog.setScene(dialogScene);
		dialog.showAndWait();     
		MineSweeper.playTimer();
	}

	/**
	 * Displays popup to show highscores for all levels
	 */
	public static void displayAllScores() {

		MineSweeper.pauseTimer();

		final Stage dialog = new Stage();
		dialog.setTitle("High Scores");
		dialog.setResizable(false);
		dialog.getIcons().add(new Image(Images.ICON.getImage()));
		BorderPane rootPane = new BorderPane();
		StackPane titlePane = new StackPane();
		BorderPane scorePane = new BorderPane();
		TilePane levelsPane = new TilePane();
		levelsPane.setPrefColumns(3);
		StackPane customPane = new StackPane();
		BorderPane buttonPane = new BorderPane();
		buttonPane.setPadding(new Insets(20));

		Label titleLabel = new Label("HIGH SCORES" + "\n\n");
		titleLabel.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 40));
		titlePane.getChildren().add(titleLabel);

		for(int i = 0; i < Level.values().length -1; i++) {
			levelsPane.getChildren().add(createScorePane(Level.values()[i], 15, 15));
		}

		customPane.getChildren().add(createScorePane(Level.CUSTOM, 15, 15));
		scorePane.setCenter(levelsPane);
		scorePane.setBottom(customPane);

		Button okButton = new Button("OK");
		okButton.setOnMouseClicked(e->{dialog.close();});
		okButton.setMinWidth(75);
		okButton.setFont(Font.font(15));
		buttonPane.setRight(okButton);

		rootPane.setTop(titlePane);
		rootPane.setCenter(scorePane);
		rootPane.setBottom(buttonPane);

		Scene dialogScene = new Scene(rootPane);
		dialog.setScene(dialogScene);
		dialog.showAndWait();     

		MineSweeper.playTimer();
	}

	/*
	 * Creates a pane displaying the scores for a given level using
	 * font and padding value input.
	 */
	private static VBox createScorePane(Level level, int fontSize, int padding) {

		VBox mainPane = new VBox();
		mainPane.setAlignment(Pos.CENTER);
		HBox scorePane = new HBox();
		scorePane.setAlignment(Pos.CENTER);
		scorePane.setPadding(new Insets(padding));
		Label levelLabel = new Label("\n" + level.getDescriptionName().toUpperCase() + "\n" + level.getLevelParameters() + "\n");
		levelLabel.setFont(Font.font(null, FontWeight.EXTRA_BOLD, fontSize));
		levelLabel.setTextAlignment(TextAlignment.CENTER);

		Label namesLabel = new Label(getHighScoreNames(level));
		namesLabel.setFont(Font.font(null, FontWeight.BOLD, fontSize));
		namesLabel.setPadding(new Insets(padding));
		scorePane.getChildren().add(namesLabel);

		Label scoresLabel = new Label(getHighScores(level));
		scoresLabel.setFont(new Font(fontSize));
		scoresLabel.setPadding(new Insets(padding));
		scoresLabel.setTextAlignment(TextAlignment.LEFT);
		scorePane.getChildren().add(scoresLabel);

		if(level.equals(Level.CUSTOM)) {
			Label levelDetailsLabel = new Label(getHighScoreDetails(level));
			levelDetailsLabel.setFont(Font.font(null, FontWeight.BOLD, fontSize));
			levelDetailsLabel.setPadding(new Insets(padding));
			levelDetailsLabel.setTextAlignment(TextAlignment.LEFT);
			scorePane.getChildren().add(levelDetailsLabel);

			levelLabel.setText("\n" + level.getDescriptionName().toUpperCase() + "\n");
		}

		mainPane.getChildren().addAll(levelLabel, scorePane);

		return mainPane;

	}

	/*
	 * Create string showing the highscore names for creating the score pane
	 */
	private static String getHighScoreNames(Level level) {
		ArrayList<Score> scores = highScores.get(Level.getLevelIndex(level));
		String outputString = "";
		for (Score score : scores) {
			outputString += score.getName() + "\n";
		}
		return outputString;
	}

	/*
	 * Create string showing the highscores for creating the score pane
	 */
	private static String getHighScores(Level level) {
		ArrayList<Score> scores = highScores.get(Level.getLevelIndex(level));
		String outputString = "";
		for (Score score : scores) {
			outputString += MineSweeper.getTimeAsString(score.getTime()) + "\n";
		}
		return outputString;
	}

	/*
	 * Create string showing the highscore level details for creating the 
	 * score pane for custom level
	 */
	private static String getHighScoreDetails(Level level) {
		ArrayList<Score> scores = highScores.get(Level.getLevelIndex(level));
		String outputString = "";
		for (Score score : scores) {
			CustomScore customScore = (CustomScore)score;
			outputString += customScore.getlevelDetailsString() + "\n";
		}
		return outputString;
	}

	/**
	 * Display confirmation to reset highscores
	 */
	public static void resetHighScoresConfirm() {
		MineSweeper.pauseTimer();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.initOwner(Main.getStage());
		alert.setTitle("Confirm");
		alert.setHeaderText("Reset highScores");
		alert.setContentText("Are you sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			resetSettings();
		}
		MineSweeper.playTimer();
	}
	
	/*
	 * Reset all scores to default
	 */
	private static void resetSettings() {
		//Initialise lists
		highScores = new ArrayList<ArrayList<Score>>() ;
		for (int i = 0; i < 7; i++) {
			highScores.add(new ArrayList<Score>());
		}
		ArrayList<Score> currentList = new ArrayList<Score>();
		for (int i = 0; i < highScores.size(); i++) {
			currentList = highScores.get(i);
			/*
			 * If setting custom levels populate list 
			 * with CustomScore objects
			 */
			if(i == 6) {
				for (int j = 0; j < 5; j++) {
					currentList.add(new CustomScore("Player", 59999, new int[] {10,10,10}));
				}
			}
			/*
			 * If not setting custom level populate list with Score objects
			 */
			else {
				for (int j = 0; j < 5; j++) {
					currentList.add(new Score("Player", 59999));
				}

			} 
		}
	}  

	/*
	 * Getters and setters
	 */
	public static File getSettingsFile() {
		return settingsFile;
	}
//
//	public static File getScoresresettextfile() {
//		return resetFile;
//	}
}

