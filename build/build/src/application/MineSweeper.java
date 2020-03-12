package application;

import gui.GUI;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import model.Level;
import model.MineField;

/**
 * Minesweeper class representing an instance of the game
 * 
 * @author Daniel Newsom
 * @version 3.0
 *
 */
public class MineSweeper {

	private MineField minefield;
	private static boolean playing;
	private GUI gui;
	private int time;
	private StringProperty timeString;
	private StringProperty level;
	private static Timeline timer;

	/**
	 * Constructor for the Minesweeper class 
	 */
	public MineSweeper() {
		createMinefield(Level.getLevel());
		playing = true;
		time = 0;
		timeString = new SimpleStringProperty("00");
		level = new SimpleStringProperty(Level.getLevel().toString());
		timer = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {incrementTime();}));
		timer.setCycleCount(Animation.INDEFINITE);
		playTimer();
	}

	/**
	 * Create a minefield based on the parameters from the current level and then populate
	 * @param level represents the difficulty level currently set.
	 */
	private void createMinefield(Level level) {
		minefield = new MineField(Level.getLevelArray(Level.getLevel()));
		minefield.populate();
	}

	/*
	 * Update the time string for the GUI timer display
	 */
	private void updateTimeString() {
		if(time < 60) {
			timeString.set(String.format("%02d", time));	
		}
		else {
			timeString.set(String.format("%02d:%02d",time / 60, time % 60));
		}
	}
	
	/**
	 * Create a string from time for the win dialog and highscores.
	 * @param time int representing the time to convert
	 * @return String representing the time taken
	 * 
	 */
	public static String getTimeAsString(int time) {
		if(time < 60) {
			if(time > 1) {
				return String.format("%s", String.format("%02d seconds", time));	
			}
			else {
				return String.format("%s", String.format("%02d second", time));	
			}
		}
		else if (time < 120)  {
			return String.format("%s",String.format("%02d minute and %02d seconds",time / 60, time % 60));
		}
		else {
			return String.format("%s",String.format("%02d minutes and %02d seconds",time / 60, time % 60));
		}
	}
	
	/**
	 * Methods to control the games timer
	 */
	public static void playTimer() {
		if(MineSweeper.isPlaying()) {
			timer.play();
		}
	}

	public static void stopTimer() {
		timer.stop();
	}

	public static void pauseTimer() {
		timer.pause();
	}
	
	/*
	 * Increment the game time field and update timeString field
	 */
	private void incrementTime(){
		time++;
		updateTimeString();
	}
	
	/*
	 * Getters and Setters
	 */
	public GUI getGui() { return gui; }

	public void setGui(GUI gui) { this.gui = gui; }

	public MineField getMinefield() { return minefield; }

	public static boolean isPlaying() { return playing; }

	public void setPlaying( boolean playing) {MineSweeper.playing = playing; }

	public int getTime() { return time; }
	
	public StringProperty getlevelString() { return level; }
	
	public StringProperty getTimeString() { return timeString; } 

}
