package main.java.Talkbox.configurer;

import java.awt.Color;
import java.io.File;
import java.util.List;

import javax.swing.UIManager;

import main.java.Talkbox.filehandler.FileIO;

/**
 * This class sets the configuration for each virtual button on the panel. Each virtual button
 * is linked to a button configuration folder in which a text-file holds information about
 * its text, color, sound file and image file.
 * @author rtalkad
 *
 */
public class ButtonConfiguration {
	public static final Color DEFAULT_COLOR = UIManager.getColor("Button.background");
	private String buttonText;
	private File buttonDir;
	private File buttonTxtDir;
	private Color buttonColor = DEFAULT_COLOR;
	private File soundFile = null;
	private File imageFile = null;

	/**
	 * Public constructor that sets the button's text, color, audio file, and button
	 * directory.
	 * 
	 * @param buttonText The text to be included.
	 * @param color      The color of the button.
	 * @param buttonDir  The path to the button configuration directory.
	 */
	public ButtonConfiguration(String buttonText, File buttonDir) {
		this.buttonText = buttonText;
		this.buttonDir = buttonDir;
		this.buttonTxtDir = new File(this.buttonDir.getPath() + FileIO.SEP + "button.txt");
		FileIO.textToFile(this.buttonTxtDir, this.buttonText + '\n' + this.buttonColor.getRGB() + "\n0\n0");
	}
	
	/**
	 * Add image file to the button configuration.
	 * 
	 * @param image The image file (JPEG or PNG).
	 */
	public void addImageFile(File image) {
		if (image != null) {
			this.imageFile = new File(this.buttonDir.getPath() + FileIO.SEP + "image" + FileIO.SEP + "image" + FileIO.getExt(image));
			FileIO.copyFile(image,this.imageFile);
			FileIO.editTextLine(this.buttonTxtDir, "1", 3);
		}
	}
	
	/**
	 * Add sound file to the button configuration.
	 * 
	 * @param f The sound file.
	 */
	public void addSoundFile(File sound) {
		if (sound != null) {
			this.soundFile = new File(this.buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
			FileIO.copyFile(sound, this.soundFile);
			FileIO.editTextLine(this.buttonTxtDir, "1", 2);
		}
	}


	/**
	 * Add color to button.
	 * 
	 * @param col The color object.
	 */
	public void addColor(Color col) {
		this.buttonColor = col;
		FileIO.editTextLine(this.buttonTxtDir, String.valueOf(this.buttonColor.getRGB()), 1);
	}

	/**
	 * Set the button's text.
	 */
	public void setText(String txt) {
		this.buttonText = txt;
		FileIO.editTextLine(this.buttonTxtDir, this.buttonText, 0);
	}

	/**
	 * Reads the button data (color, text, and sound if it exists) from a .txt file
	 * and returns a button configuration.
	 * 
	 * @param buttonDir The location of the directory.
	 * @return a ButtonConfiguration with the given configuration.
	 */
	public static ButtonConfiguration readButtonTxt(File buttonDir) {
		List<String> input = FileIO.readTextFile(new File(buttonDir + FileIO.SEP + "button.txt"));
		// Reading text file for button text, color, sound, and image
		File sound = null;
		File image = null;
		if (Integer.parseInt(input.get(2)) == 1) {
			sound = new File(buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
		}
		if (Integer.parseInt(input.get(3)) == 1) {
			image = FileIO.getAllFiles("image", new File(buttonDir.getPath() + FileIO.SEP + "image")).get(0);
		}
		ButtonConfiguration bc = new ButtonConfiguration(input.get(0), buttonDir);
		bc.addColor(new Color(Integer.parseInt(input.get(1))));
		bc.addSoundFile(sound);
		bc.addImageFile(image);
		return bc;
	}


	/**
	 * Changes the directory of the button and its sound and image files.
	 * 
	 * @param newDir The new button_config_ directory.
	 */
	public void changeDirectory(File newDir) {
		boolean success = this.buttonDir.renameTo(newDir);
		if (!(success)) {
			throw new RuntimeException("Error");
		}
		this.buttonDir = newDir;
		this.buttonTxtDir = new File(newDir.getPath() + FileIO.SEP + "button.txt");
		if (this.soundFile != null) {
			File newSound = new File(this.buttonDir.getPath() + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
			FileIO.copyFile(this.soundFile, newSound);
			this.soundFile = newSound;
		}
		if (this.imageFile != null) {
			File newImage = new File(this.buttonDir.getPath() + FileIO.SEP + "image" + FileIO.SEP + "image" + FileIO.getExt(this.imageFile));
			FileIO.copyFile(this.imageFile, newImage);
			this.imageFile = newImage;
		}
	}


	/**
	 * String representation of buttons. Used in testing.
	 */
	@Override
	public String toString() {
		String audioName = this.soundFile != null ? this.soundFile.getName() : "null";
		String imageName = this.imageFile != null ? this.imageFile.getName() : "null";
		String format = String.format("Audio file is [%s]\nSound file is [%s]\nColor is [%s]\nText is [%s]\n",
				audioName, imageName, this.buttonColor.toString(), this.buttonText);
		return format;
	}
	public String tempImagePath() {
		return this.buttonDir.getPath() + FileIO.SEP + "image" + FileIO.SEP + "temp.png";
	}
	public String tempAudioPath() {
		return this.buttonDir.getPath() + FileIO.SEP + "sound" + FileIO.SEP + "temp.wav";
	}
	/**
	 * @return The button's color.
	 */
	public Color getButtonColor() {
		return this.buttonColor;
	}

	/**
	 * @return The button's text.
	 */
	public String getButtonText() {
		return this.buttonText;
	}

	/**
	 * @return The directory to this button configuration.
	 */
	public File returnDir() {
		return this.buttonDir;
	}
	public File getImageFile() {
		return this.imageFile;
	}
	/**
	 * @return The sound file of the button.
	 */
	public File getSoundFile() {
		return this.soundFile;
	}
}

