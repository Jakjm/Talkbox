package configurer;

import java.awt.Color;
import java.io.File;

import javax.swing.UIManager;

public class ButtonConfiguration {
	public String buttonText;
	public Color buttonColor;
	public File soundFile;
	/** TODO Rohan create constructor that includes buttonDir that is created as part of configuration**/
	public File buttonDir;
	public static final Color DEFAULT_COLOR =  UIManager.getColor("Button.background");
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
	public ButtonConfiguration(String buttonText) {
		this(buttonText,null,null);
	}
	/**
	 * TODO Rohan
	 * The buttonDir is the location of the button config directory
	 * Writes the button text and button color a button config text file inside the directory (overwrites if already exists)
	 * Copies the sound file, if it is not null, to the directory as "sound.wav"
	 */
	public void writeTo() {
		
	}
	/**
	 * TODO Rohan 
	 * The buttonDir is the location of the directory
	 * Reads the button data (color, text, and sound if it exists from the sound file - if "sound.wav" exists).
	 * @param the button directory
	 */
	public static ButtonConfiguration readFrom(File buttonDir) {
		return null;
	}
}
