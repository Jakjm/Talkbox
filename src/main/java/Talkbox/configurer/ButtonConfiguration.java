package main.java.Talkbox.configurer;

import java.awt.Color;
import java.io.File;

import javax.swing.UIManager;

import main.java.Talkbox.filehandler.FileIO;

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
	 * @param color		 The color of the button.
	 * @param soundFile  The sound file associated with the button.
	 * @param buttonDir  The path to the button configuration directory.
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
	public void setButtonValues(String text,Color color,File audioFile) {
		if(color == null)this.buttonColor = DEFAULT_COLOR;
		else this.buttonColor = color;
		this.buttonText = text;
		this.soundFile = audioFile;
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
		String data = this.buttonText + '\n' + this.buttonColor.getRGB() + '\n';
		// if the sound file is not null copy it to the sound directory
		if (this.soundFile != null) {
			File internalFile = new File(this.buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
			FileIO.copyFile(soundFile, internalFile);
			this.soundFile = internalFile;
			data += 1;
		} else {
			data += 0;
		}
		// write to file
		FileIO.createTextFile(new File(textDir), data);
	}
	
	/**
	 * Add sound file to the button configuration.
	 * @param f The sound file.
	 */
	public void addSoundFile(File f) {
		this.soundFile = f;
		this.writeButtonTxt();
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
		Color col = new Color(Integer.parseInt(input[1]));
		if (Integer.parseInt(input[2]) == 1) {
			sound = new File(buttonDir + FileIO.SEP + "sound" + FileIO.SEP + "sound.wav");
		}
		return new ButtonConfiguration(input[0], col, sound, buttonDir);
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
		if(!success) {
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
	 * String representation of buttons. Used in testing.
	 */
	@Override
	public String toString() {
		String audioName = this.soundFile != null ? this.soundFile.getName() : "null";
		String format = String.format("Audio file is [%s]\nColor is [%s]\nText is [%s]\n", audioName, this.buttonColor.toString(), this.buttonText);
		return format;
	}
}