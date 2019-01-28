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
	/** TODO Rohan create constructor that includes buttonDir that is created as part of configuration**/
	public File buttonDir;
	public static final Color DEFAULT_COLOR =  UIManager.getColor("Button.background");
	/**
	 * Public constructor that sets the button's text, color, and audio file.
	 * @param buttonText The text to be included.
	 * @param color The color of the button.
	 * @param soundFile The sound file associated with the button.
	 */
	public ButtonConfiguration(String buttonText,Color color,File soundFile) {
		if(color == null) {
			buttonColor = DEFAULT_COLOR;
		}
		else {
			buttonColor = color;
		}
		this.soundFile = soundFile;
		this.buttonText = buttonText;
	}
	/**
	 * Adds button text.
	 * @param buttonText
	 */
	public ButtonConfiguration(String buttonText) {
		this.buttonText = buttonText;
	}
	/**
	 * Adds the sound file.
	 */
	public void addSoundFile(File sound) {
		this.soundFile = sound;
	}
	/**
	 * TODO Jordan change color given RGB
	 */
	public void addColor() {
		
	}
	/**
	 * Public constructor that sets the button's text, color, audio file, and button directory.
	 * @param buttonText The text to be included.
	 * @param color The color of the button.
	 * @param soundFile The sound file associated with the button.
	 */
	public ButtonConfiguration(String buttonText,Color color,File soundFile, File buttonDir) {
		this(buttonText, color, soundFile);
		this.buttonDir = buttonDir;
	}
	/**
	 * Used for updating and setting the button configuration. The buttonDir is the location of the button config directory.
	 * Writes the button text, color, and 1 or 0 representing whether there is an associated sound file set.
	 * Copies the sound file, if it is not null, to the directory as "sound.wav"
	 */
	public void writeTo() {
		String data = this.buttonText + "\t" + this.buttonColor.getRGB() + "\t";
		File defaultDir = this.buttonDir.getParentFile();
		// if the sound file is not null move it to the sound directory
		if (this.soundFile != null) {
			FileIO.moveFile(soundFile, this.buttonDir + FileIO.SEP + "sound");
			data  += 1;
		}
		else {
			data += 0;
		}
		FileIO.createTextFile(defaultDir, data);
	}
	/**
	 * TODO Rohan 
	 * Reads the button data (color, text, and sound if it exists from the sound file - if "sound.wav" exists).
	 * @param the button directory The buttonDir is the location of the directory
	 * @return a ButtonConfiguration with the given configuration.
	 */
	public static ButtonConfiguration readFrom(File buttonDir) {
		String[] input = FileIO.readTextFile(new File(buttonDir + FileIO.SEP + "button.txt"));
		File sound = null;
		Color col = new Color(Integer.parseInt(input[0]));
		// if no sound is given make the sound file null
		if (Integer.parseInt(input[2]) == 1) {
			sound = new File(buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
		}
		return new ButtonConfiguration(input[0], col, sound);
		
	}
}
