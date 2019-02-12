package configurer;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;

import javax.swing.UIManager;

import filehandler.FileIO;

public class ButtonConfiguration {
	public String buttonText;
	public Color buttonColor;
	public File soundFile;
	public File buttonDir;
	public static final Color DEFAULT_COLOR = UIManager.getColor("Button.background");

	/**
	 * Public constructor that sets the button's text, color, audio file, and button
	 * directory.
	 * 
	 * @param buttonText The text to be included.
	 * @param color      The color of the button.
	 * @param soundFile  The sound file associated with the button.
	 * @param The        directory of the button.
	 */
	public ButtonConfiguration(String buttonText, Color color, File soundFile, File buttonDir) {
		if (color == null) {
			buttonColor = DEFAULT_COLOR;
		} else {
			buttonColor = color;
		}
		this.soundFile = soundFile;
		this.buttonText = buttonText;
		this.buttonDir = buttonDir;
		this.writeButtonTxt();
	}

	/**
	 * @return The directory to this button configuration.
	 */
	public File returnDir() {
		return this.buttonDir;
	}

	/**
	 * Used for updating and setting the button configuration. The buttonDir is the
	 * location of the button config directory. Writes the button text, color in
	 * RGB, and 1 or 0 representing whether there is an associated sound file.
	 * Copies the sound file, if it is not null, to the directory as "sound.wav"
	 * 
	 */
	public void writeButtonTxt() {
		// create string path to button.txt
		String textDir = this.buttonDir + FileIO.SEP + "button.txt";
		String data = this.buttonText + "\t";
		data += this.buttonColor.getRGB() + "\t";
		// if the sound file is not null copy it to the sound directory
		if (this.soundFile != null) {
			FileIO.copyFile(soundFile, this.buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
			data += 1;
		} else {
			data += 0;
		}
		// write to file
		FileIO.createTextFile(new File(textDir), data);
	}

	/**
	 * Reads the button data (color, text, and sound if it exists) from a 
	 * .txt file and returns a button configuration.
	 * 
	 * @param buttonDir The location of the directory.
	 * @return a ButtonConfiguration with the given configuration.
	 */
	public static ButtonConfiguration readButtonTxt(File buttonDir) {
		String[] input = FileIO.readTextFile(new File(buttonDir + FileIO.SEP + "button.txt"));
		// receive name of the button, the color, and whether it has a sound file or not
		File sound = null;
		Color col = new Color(Integer.parseInt(input[1]));
		if (Integer.parseInt(input[2]) == 1) {
			sound = new File(buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
		}
		return new ButtonConfiguration(input[0], col, sound, buttonDir);
	}
}
