package gui;

import application.Main;
import application.MineSweeper;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.HighScores;
import model.Level;

/**
 * GUI Class to represent the main GUI of the game
 * 
 * @author Daniel Newsom
 * @version 3.0
 *
 */

public class GUI {

	private MenuBar menuBar;
	private ToggleGroup levelRadios;
	private VBox mainWindow;
	private TilePane mainGameField;
	private TilePane statusPane;
	private HBox levelPane;
	private MineSweeper game;
	

	/**
	 * Constructor for the GUI of the game
	 * @param game represents the current game
	 */
	public GUI(MineSweeper game) {
		this.game = game;
		game.setGui(this);
	}

	/**
	 * Construct the GUI for the game
	 */
	public void constructWindow() {
		/*
		 * create the root pane
		 */
		BorderPane root = new BorderPane();

		/*
		 * Create the required panes
		 */
		constructMenu();
		constructMainWindow();
		drawLevelPane();

		/*
		 * Add all panes to the root pane
		 */
		root.setTop(menuBar);
		root.setCenter(mainWindow);
		root.setBottom(levelPane);

		/*
		 * Add root pane to the stage and display
		 */
		Scene scene = new Scene(root);
		Main.getStage().setScene(scene);
		Main.getStage().show();		

	}

	/*
	 * Construct the menu bar
	 */
	private void constructMenu() {
		menuBar = new MenuBar();
		levelRadios = new ToggleGroup();
		Menu gameMenu = new Menu("_Game");
		Menu levelsMenu = new Menu("_Level");
		Menu highscoresMenu = new Menu("_High Scores");

		/*
		 * Constructs the Game menu and adds the New Game and Quit items
		 * Also sets the event handlers and shortcuts for these items
		 */
		MenuItem newGame = new MenuItem("_New Game");
		newGame.setOnAction(e->{Main.restartgame();});
		newGame.setAccelerator(new KeyCodeCombination(KeyCode.N,KeyCombination.CONTROL_DOWN));

		MenuItem quit = new MenuItem("_Quit");
		quit.setOnAction(e->{Main.exitGame();});
		quit.setAccelerator(new KeyCodeCombination(KeyCode.Q,KeyCombination.CONTROL_DOWN));
		
		gameMenu.getItems().addAll(newGame, quit);
		
		
		/*
		 * Create and add a menu item for each level in the Level enum and adds it to a toggle group.
		 * Also sets the event handler for the item.
		 */
		for(Level level: Level.values()) {
			RadioMenuItem temp = new RadioMenuItem(level.toString());
			temp.setUserData(level);
			temp.setOnAction(e->{Level.changeLevel(e,MineSweeper.isPlaying()); selectLevelMenuItem();});
			temp.setAccelerator(new KeyCodeCombination(Level.getKeyCode(level), KeyCombination.CONTROL_DOWN));
			levelsMenu.getItems().add(temp);
			levelRadios.getToggles().add(temp);
		}
		selectLevelMenuItem();

		MenuItem viewLevel = new MenuItem("_View current level");
		viewLevel.setOnAction(e->{HighScores.displayLevelScores(Level.getLevel());});

		MenuItem viewAll = new MenuItem("View _all");
		viewAll.setOnAction(e->{HighScores.displayAllScores();});
		
		MenuItem reset = new MenuItem("_Reset all");
		reset.setOnAction(e->{HighScores.resetHighScoresConfirm();});
		highscoresMenu.getItems().addAll(viewLevel, viewAll, reset);

		/*
		 * Add both menus to the menu bar
		 */
		menuBar.getMenus().addAll(gameMenu, levelsMenu, highscoresMenu);

	}

	/*
	 * Selects the correct level menu item based on the currently selected level
	 */
	private void selectLevelMenuItem() {

		ObservableList<Toggle> levelsList = levelRadios.getToggles();
		for(int i = 0; i < levelsList.size();i++) {
			RadioMenuItem current = (RadioMenuItem) levelsList.get(i);
			if(current.getUserData() == Level.getLevel()) {
				current.setSelected(true);	
				break;
			}
		}
	}

	/*
	 * Create the main game window (minefield and timer panes)
	 */
	private void constructMainWindow() {
		mainWindow = new VBox();
		mainWindow.setAlignment(Pos.CENTER);
		createMainGameField();
		constructStatusPane();
		ScrollPane mainGamePane = new ScrollPane(mainGameField);
		mainWindow.getChildren().addAll(statusPane, mainGamePane);
	}
	
	/*
	 * Constructs status pane to show flags placed, game time and mines remaining
	 * Binds labels to relevent variables
	 */
	private void constructStatusPane() {

		statusPane = new TilePane();
		statusPane.setAlignment(Pos.CENTER);
		statusPane.setMaxWidth(290);
		HBox tilesFlaggedPane = new HBox();
		tilesFlaggedPane.setAlignment(Pos.CENTER);
		tilesFlaggedPane.setPadding(new Insets(20));
		StackPane timePane = new StackPane();
		timePane.setPadding(new Insets(10, 0, 10, 0));
		HBox minesRemainingPane = new HBox();
		minesRemainingPane.setAlignment(Pos.CENTER);
		minesRemainingPane.setPadding(new Insets(20));
		
		ImageView tilesFlaggedImage = new ImageView(Images.FLAG.getImage());
		Label tilesFlaggedLabel = new Label();
		tilesFlaggedLabel.setFont(new Font(20));
		tilesFlaggedLabel.textProperty().bind(game.getMinefield().getTilesFlagged());
		tilesFlaggedPane.getChildren().addAll(tilesFlaggedImage, tilesFlaggedLabel);
		
		ImageView timeImage = new ImageView(Images.CLOCK.getImage());
		Label timeLabel = new Label();
		timeLabel.setFont(new Font(20));
		timeLabel.textProperty().bind(game.getTimeString());
		timePane.getChildren().addAll(timeImage, timeLabel);
		
		ImageView minesRemainingImage = new ImageView(Images.MINE.getImage());
		Label minesRemainingLabel = new Label();
		minesRemainingLabel.setFont(new Font(20));
		minesRemainingLabel.textProperty().bind(game.getMinefield().getMinesRemaining());
		minesRemainingPane.getChildren().addAll(minesRemainingImage, minesRemainingLabel);
		
		statusPane.getChildren().addAll(tilesFlaggedPane, timePane, minesRemainingPane);
		
	}

	/*
	 * Constructs the main minefield representaion
	 */
	private void createMainGameField() {
		mainGameField = new TilePane(Orientation.VERTICAL);
		mainGameField.setPrefRows(game.getMinefield().getRows());
		mainGameField.setPadding(new Insets(15));
		mainGameField.setAlignment(Pos.CENTER);
		
		/*
		 * Create a button for each minetile and set the event handler
		 */
		for(int row = 0; row < game.getMinefield().getRows(); row++) {
			for(int col = 0; col < game.getMinefield().getCols(); col++) {
				TileButton tileButton = new TileButton(row,col,game.getMinefield().getMineTile(row,col), game);
				tileButton.setPadding(new Insets(2));
				tileButton.setMinSize(30, 30);
				tileButton.setOnMouseClicked(e->{tileButton.clickOnTile(e);});
				mainGameField.getChildren().add(tileButton);

			}
		}
	}

	/*
	 * Constructs pane to display current level and bind label to variable
	 */
	private void drawLevelPane() {
		levelPane = new HBox();
		levelPane.setAlignment(Pos.CENTER);
		Label levelLabel = new Label();
		levelLabel.textProperty().bind(game.getlevelString());
		levelLabel.setFont(new Font(15));
		levelLabel.setPadding(new Insets(10));;
		levelPane.getChildren().add(levelLabel);
	}
	
	/**
	 * Refreshes all tileButtons to display relevant image.
	 */
	public void refreshGameField() {

		for(int i = 0; i<game.getMinefield().getTotalTiles();i++) {
			ObservableList<Node> tiles = mainGameField.getChildren();
			TileButton tile = (TileButton)tiles.get(i);
			tile.setButtonContent();
		}
	}
}


