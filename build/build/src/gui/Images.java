package gui;

/**
 * Images Enum to represent the different images in the game
 * 
 * @author Daniel Newsom
 * @version 3.0
 */
public enum Images {
	FLAG("images/flag.png"), 
	MINE("images/mine.png"),
	CLOCK("images/clock.png"),
	ICON("images/icon.png");

	private String imagePath;
	
	/*
	 * Constructor for the Image Enum
	 */
	Images(String imagePath) {
		this.imagePath = imagePath;
	}
	
	/**
	 * return a string representing the image path for the parameters value 
	 * @return ImageView object containing the image to use
	 */
	public String getImage() {
		return imagePath;
	}
}
