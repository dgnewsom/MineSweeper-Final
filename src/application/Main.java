package application;

import java.util.Optional;

import com.sun.javafx.application.LauncherImpl;

import gui.GUI;
import gui.Images;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.HighScores;

/**
 * Main Class for the minesweeper game
 * 
 * @author Daniel Newsom
 * @version 3.0
 *
 */
public class Main extends Application {

	private static Stage stage;
	private static final int COUNT_LIMIT = 100;
	
	public static void main(String[] args) {
		//Load splash screen
		LauncherImpl.launchApplication(Main.class, SplashPreloader.class, args);
	}

	@Override
	public void init() throws Exception {
		//count and pass value to the Splash preLoader
		for (int i = 1; i <= COUNT_LIMIT; i ++) {
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(i));
            Thread.sleep(30);
        }
	}

	@Override
	public void start(Stage stage){
		/*
		 * Set up the main stage and start a new game
		 */
		stage.setOnCloseRequest(e->{HighScores.exportSettingsToFile();});
		Main.stage = stage;
		stage.getIcons().add(new Image(Images.ICON.getImage()));
		stage.setTitle("MineSweeper");
		HighScores.importSettingsFromFile();
		startGame(stage);
	}

	/**
	 * method to create a new game instance
	 * @param stage representing the main stage
	 */
	public static void startGame(Stage stage) {

		MineSweeper game = new MineSweeper();
		GUI gui = new GUI(game);
		gui.constructWindow();
	}

	/**
	 * Display a dialog to confirm and then restart game if playing
	 * if not playing just restart game
	 */
	public static void restartgame() {
		Alert alert;
		Optional<ButtonType> result;
		/*
		 * If the game is currently playing display confirmation before starting a new game
		 * if not start a new game.
		 */
		MineSweeper.pauseTimer();
		if(MineSweeper.isPlaying()){
			alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.getStage());
			alert.setTitle("Confirm");
			alert.setHeaderText("Starting new Game");
			alert.setContentText("Are you sure?");
			alert.setResizable(false);
			
			result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				startGame(Main.getStage());
			}
		}
		else {
			startGame(Main.getStage());
		}
		MineSweeper.playTimer();

	}
	
	/**
	 * Method to confirm exit and save settings on exit
	 */
	public static void exitGame() {
		/*
		 * If the game is currently playing display confirmation before quitting the game
		 * if not quit the game
		 */
		Alert alert;
		Optional<ButtonType> result;
		MineSweeper.pauseTimer();
		if(MineSweeper.isPlaying()){
			alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(Main.getStage());
			alert.setTitle("Confirm");
			alert.setHeaderText("Quitting game");
			alert.setContentText("Are you sure?");
			alert.setResizable(false);

			result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				//save settings before closing
				HighScores.exportSettingsToFile();
				System.exit(0);
			}
		}
		else {
			//save settings before closing
			HighScores.exportSettingsToFile();
			Main.exitGame();
		}
		MineSweeper.playTimer();
	}

	/*
	 * Getters and setters
	 */
	public static Stage getStage() {
		return Main.stage;
	}


}
