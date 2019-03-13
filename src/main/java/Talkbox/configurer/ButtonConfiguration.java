package main.java.Talkbox.configurer;

import java.awt.Color;
import java.io.File;

import javax.swing.UIManager;

import main.java.Talkbox.filehandler.FileIO;

/**
 * TODO: Constructors were too long with lots of parameters. Specifically we are initializing a lot of things at different
 * times (like the sound and image file). So I've made getters instead. Because of this, every time we're done building a 
 * ButtonConfiguration, we'll have to call writeButtonTxt() to update the button.txt file. This is incomplete btw.
 * @author rtalkad
 *
 */
public class ButtonConfiguration {
	public static final Color DEFAULT_COLOR = UIManager.getColor("Button.background");
	private String buttonText;
	private Color buttonColor = DEFAULT_COLOR;
	private File soundFile;
	private File buttonDir;
	private File imageFile;

	/**
	 * Public constructor that sets the button's text, color, audio file, and button
	 * directory.
	 * 
	 * @param buttonText The text to be included.
	 * @param color		 The color of the button.
	 * @param buttonDir  The path to the button configuration directory.
	 */
	public ButtonConfiguration(String buttonText, File buttonDir) {
		this.buttonText = buttonText;
		this.buttonDir = buttonDir;
		this.imageFile = null;
		this.writeButtonTxt();
	}
	
	/**
	 * Add sound file to the button configuration.
	 * @param f The sound file.
	 */
	public void addSoundFile(File f) {
		if (f != null) {
			this.soundFile = new File(this.buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
			FileIO.copyFile(f, this.soundFile);
		}
	}
	
	/**
	 * Add image file to the button configuration.
	 */
	public void addImageFile(File f) {
		this.imageFile = f;
	}
	/**
	 * Add color to button.
	 */
	public void addColor(Color col) {
		this.buttonColor = col;
	}
	/**
	 * Set the button's text.
	 */
	public void setText(String txt) {
		this.buttonText = txt;
	}

	/**
	 * Reads the button data (color, text, and sound if it exists) from a .txt file
	 * and returns a button configuration.
	 * 
	 * @param buttonDir The location of the directory.
	 * @return a ButtonConfiguration with the given configuration.
	 */
	public static ButtonConfiguration readButtonTxt(File buttonDir) {
		String[] input = FileIO.readTextFile(new File(buttonDir + FileIO.SEP + "button.txt"));
		// receive name of the button, the color, and whether it has a sound file or not
		File sound = null;
		File image = null;
		if (Integer.parseInt(input[2]) == 1) {
			sound = new File(buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
		}
		if (Integer.parseInt(input[3]) == 1) {
			//TODO: 
			image = new File(buttonDir + FileIO.SEP + "image" + FileIO.SEP + "image.*");
		}
		ButtonConfiguration bc = new ButtonConfiguration(input[0], buttonDir);
		bc.addColor(new Color(Integer.parseInt(input[1])));
		bc.addSoundFile(sound);
		bc.addImageFile(image);
		return bc;
	}
	/**
	 * Returns the sound file of the button.
	 */
	public File getSoundFile() {
		return this.soundFile;
	}
	
	/**
	 * Changes the directory of the button and its sound file.
	 * @param newDir The new button_config_ directory.
	 */
	public void changeDirectory(File newDir) {
		boolean success = this.buttonDir.renameTo(newDir);
		if(!(success)) {
			throw new RuntimeException("Error");
		}
		// change the directory of the audio file and button_config_
		else {
			this.buttonDir = newDir;
			if(this.soundFile != null) {
				this.addSoundFile(new File(this.buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav"));
			}
		}
	}
	/**
	 * Updates the button.txt file's button text, color in RGB, two binary integers representign
	 * whether there are associated sound and image files with the button.
	 * 
	 */
	public void writeButtonTxt() {
		String textDir = this.buttonDir + FileIO.SEP + "button.txt";
		StringBuilder config = new StringBuilder(this.buttonText + '\n' + this.buttonColor.getRGB() + '\n');
		config.append(this.soundFile != null ? "1" : "0");
		config.append(this.imageFile != null ? "1" : "0");
		FileIO.createTextFile(new File(textDir), config.toString());
	}
	
	/**
	 * String representation of buttons. Used in testing.
	 */
	@Override
	public String toString() {
		String audioName = this.soundFile != null ? this.soundFile.getName() : "null";
		String format = String.format("Audio file is [%s]\nColor is [%s]\nText is [%s]\n", audioName, this.buttonColor.toString(), this.buttonText);
		return format;
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
}
