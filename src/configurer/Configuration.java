package configurer;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import filehandler.FileIO;

import talkbox.TalkBoxConfiguration;

/**
 * Configuration for the Talkbox interface. Creates a directory to store
 * configurations for each button. Each configuration contains a serialized
 * configuration object, the associated "sound" folder with WAVE files, and
 * optionally an image file in the "image" folder.
 *
 * @author Alberto Mastrofrancesco
 * @author Jordan Malek
 * @author Rohan Talkad
 * @version 1
 */
public class Configuration implements TalkBoxConfiguration {
	private static final long serialVersionUID = -8081539415877004914L;
	// by default the directory to store the audio library is the Desktop
	private static String defaultDir = System.getProperty("user.home").concat(FileIO.SEP + "Desktop" + FileIO.SEP + "TalkboxData");
	// number of virtual buttons supported
	private int activeButtons = 6;
	// number of physical audio buttons supported
	private int totalButtons;
	// number of audio files
	private int audioSets;
	// array of buttonconfigs
	private ButtonConfiguration[] buttonConfigs;

	/**
	 * Constructor for configuration instance. Creates the directory folder with button configuration
	 * folders. The default number of buttons is six.
	 * 
	 */
	private Configuration() {
		this.buttonConfigs = new ButtonConfiguration[this.activeButtons];
		// create the directories for the buttons and the serialized config
		new File(defaultDir).mkdirs();
		new File(defaultDir.concat(FileIO.SEP + "serialized_config")).mkdir();
		// create all button directories
		this.createButtonConfigsDirs();
	}
	
	/**
	 * Static factory method that returns Configuration instance. If a configuration
	 * directory exists, creates a new directory with a higher number as the suffix to
	 * prevent overwriting configuration.
	 */
	public static Configuration getConfiguration() {
		if (new File(defaultDir).exists()) {
			if (defaultDir.charAt(defaultDir.length() - 1) == ')') {
				String copyNumber = defaultDir.substring(defaultDir.indexOf(('(')) + 1, defaultDir.length() - 1);
				defaultDir = defaultDir.substring(0, defaultDir.indexOf(('(')));
				System.out.println(copyNumber);
				defaultDir += "(" + (Integer.parseInt(copyNumber) + 1) + ")";
			}
			else {
				defaultDir += "(" + 1 + ")";
			}
		}
		return new Configuration();
	}

	/**
	 * Serialize the current Configuration instance. File is placed in the default
	 * directory in the "serialized_config" folder.
	 */
	public void serializeConfig() {
		ConfigSerialization.serialize(defaultDir.concat(FileIO.SEP + "serialized_config"+ FileIO.SEP + "config.tbc"), this);
	}

	/**
	 * Create specified amount of button configuration directories in the default
	 * directory.
	 */
	public void createButtonConfigsDirs() {
		// make empty directories for active buttons
		for (int i = 0; i < this.activeButtons; i++) {
			addButtonConfig(i);
		}
	}

	/**
	 * Add specified button config folder. Adds image and sound files.
	 * 
	 * @param i The button number.
	 */
	public void addButtonConfig(int i) {
		String dir = defaultDir.concat(FileIO.SEP + "button_config_").concat(String.valueOf(i));
		new File(dir).mkdir();
		// add sound and image folders
		new File(dir.concat(FileIO.SEP + "sound")).mkdir();
		new File(dir.concat(FileIO.SEP + "image")).mkdir();
		// create new button configuration: this creates the text file with the associated sound, color, and image
		ButtonConfiguration b = new ButtonConfiguration("Button " + i, null, null, new File(dir));
		// add button config to the array
		this.buttonConfigs[i] = b;
		// write the text file
		b.writeButtonTxt();
	}

	/**
	 * Set the default directory path.
	 * 
	 * @param dir The path to the TalkBoxData folder
	 */
	public void setDefaultDir(String dir) {
		defaultDir = dir;
	}

	/**
	 * Set the number of active buttons. If new active
	 * buttons are needed, adds the remaining number of active button config folders.
	 * 
	 * @param activeButtons
	 */
	public void setActiveButtons(int buttons) {
		for (int i = this.activeButtons; i < buttons; i++) {
			this.addButtonConfig(i);
		}
		this.activeButtons = buttons;
	}

	/**
	 * Set the number of possible buttons.
	 * 
	 * @param totalButtons
	 */
	public void setTotalButtons(int totalButtons) {
		this.totalButtons = totalButtons;
	}

	/**
	 * Set the number of audio files for each button.
	 * 
	 * @param audioSets The number of files for each button.
	 */
	public void setAudioSets(int audioSets) {
		this.audioSets = audioSets;
	}

	/**
	 * Adds image file to specified button.
	 * 
	 * @param button The button number.
	 * @param image  The image file.
	 */
	public void addImageFile(int button, File image) {
		// if image file is verified, add it to the config file
		String destination = defaultDir + FileIO.SEP + "config_" + button + FileIO.SEP + "image";
		if (button < this.activeButtons && FileIO.checkImageFile(image)) {
			FileIO.moveFile(image, destination);
		}
	}

	/**
	 * Adds image file to specified button.
	 * 
	 * @param button The button number.
	 * @param path   The path to the image file.
	 */
	public void addImageFile(int button, String path) {
		// convert path to file
		File imageFile = new File(path);
		this.addImageFile(button, imageFile);
	}

	/**
	 * Adds audio file to specified button.
	 * 
	 * @param button The button number.
	 * @param file   The sound file.
	 */
	public void addAudioFile(int button, File audio) {
		// if audio file is verified, add it to the config file
		String destination = defaultDir + FileIO.SEP + "config_" + button + FileIO.SEP + "sound";
		if (button < this.activeButtons && FileIO.checkFileFormat(audio)) {
			FileIO.moveFile(audio, destination);
		}
	}

	/**
	 * Adds audio file to specified button.
	 * 
	 * @param button    The button number.
	 * @param audioPath The string path to the audiopath.
	 */
	public void addAudioFile(int button, String audioPath) {
		File audio = new File(audioPath);
		addAudioFile(button, audio);
	}
	/**
	 * Returns the button configuration objects.
	 */
	public ButtonConfiguration[] getButtonConfigs() {
		return this.buttonConfigs;
	}

	/**
	 * Returns the number of active buttons that when pressed will play an audio
	 * file.
	 * 
	 * @return int A positive integer.
	 */
	@Override
	public int getNumberOfAudioButtons() {
		return this.activeButtons;
	}

	/**
	 * Returns the number of sets of audio files that this configuration supports
	 * for each button.
	 * 
	 * @return int A positive integer
	 */
	@Override
	public int getNumberOfAudioSets() {
		return this.audioSets;
	}

	/**
	 * Returns the total number of buttons in this TalkBox.
	 * 
	 * @return int A positive integer.
	 */
	@Override
	public int getTotalNumberOfButtons() {
		return this.totalButtons;
	}

	/**
	 * Returns a Path relative to this configuration object. The audio files are
	 * contained within the various config folders for each button
	 * 
	 * @return Path A Path object that identifies the directory that contains the
	 *         the config folders for each button.
	 */
	@Override
	public Path getRelativePathToAudioFiles() {
		return Paths.get(defaultDir);
	}

	/**
	 * Returns a 2-dimensional array of Strings that contains the names of all audio
	 * files. Each row of the array is an audio set. The dimensions of the array are
	 * given by {@link #getNumberOfAudioButtons() getNumberOfAudioButtons} and
	 * {@link #getNumberOfAudioSets() getNumberOfAudioSets}
	 * 
	 * @return A 2-dimensional array of Strings
	 */
	@Override
	public String[][] getAudioFileNames() {
		String[][] audioFileNames = new String[this.activeButtons][this.audioSets];
		// sound folder for each button
		File soundFolder;
		// filter for WAVE files
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".wav") || name.endsWith(".wave");
			}
		};
		// get WAVE files for each button
		for (int i = 0; i < this.activeButtons; i++) {
			soundFolder = new File(defaultDir + (FileIO.SEP + "config_") + i);
			for (int j = 0; j < this.audioSets; j++) {
				audioFileNames[i][j] = soundFolder.list(filter)[j];
			}
		}
		return audioFileNames;
	}
}
