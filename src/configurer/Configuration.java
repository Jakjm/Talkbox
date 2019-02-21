package configurer;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
 * @version Feb 19th 2019
 */
public class Configuration implements TalkBoxConfiguration {
	private static final long serialVersionUID = -8081539415877004914L;
	// number of physical audio buttons supported
	private int totalButtons = 6;
	// each audio set is a row of six buttons
	private int audioSets;
	// array of buttonconfigs
	public transient ButtonConfiguration[] buttonConfigs;
	// path to the talkbox directory
	private transient String talkboxPath;

	/**
	 * <b> This constructor is only used for creating a new configuration </b>
	 * Constructor for configuration instance. Creates the directory folder with
	 * button configuration folders. The default number of buttons is six.
	 * 
	 * @param path the directory in which the talkboxData directory is being made in
	 */
	public Configuration(String path) {
		this.audioSets = 1;
		this.talkboxPath = path + FileIO.SEP + "talkboxData";
		this.buttonConfigs = new ButtonConfiguration[this.totalButtons];
		// create the directories for the buttons and the serialized config
		new File(this.talkboxPath).mkdir();
		new File(this.talkboxPath + FileIO.SEP + "serialized_config").mkdir();
		// create all button directories
		this.createButtonConfigsDirs();
		this.serializeConfig();
	}

	/**
	 * <b> This method is for fully reading a configuration from a file. </b>
	 * 
	 * @param file - the file of the talkboxData directory
	 * @return the configuration complete with the button configurations, etc
	 */
	public static Configuration readConfiguration(File file) {
		try {
			// Reading the serialized object
			String serializedObject = file.getPath() + FileIO.SEP + "serialized_config" + FileIO.SEP + "config.tbc";
			Configuration config = ConfigSerialization.deserialize(serializedObject);
			// setting the talkbox path (unserialized)
			config.talkboxPath = file.getPath();
			// Reading the button configurations
			config.buttonConfigs = new ButtonConfiguration[config.totalButtons];
			String buttonPath = file.getPath() + FileIO.SEP + "button_config_";
			for (int i = 0; i < config.totalButtons; i++) {
				File buttonFile = new File(buttonPath + i);
				config.buttonConfigs[i] = ButtonConfiguration.readButtonTxt(buttonFile);
			}
			// serialize the configuration
			config.serializeConfig();
			return config;
		}
		// If there are any errors with the reading, return null.
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Unable to read configuration - please try again");
			return null;
		}
	}

	/**
	 * Serializes the current Configuration instance. File is placed in the default
	 * directory in the "serialized_config" folder.
	 */
	public void serializeConfig() {
		ConfigSerialization.serialize(talkboxPath.concat(FileIO.SEP + "serialized_config" + FileIO.SEP + "config.tbc"),
				this);
	}

	/**
	 * Creates specified amount of button configuration directories in the default
	 * directory.
	 */
	public void createButtonConfigsDirs() {
		for (int i = 0; i < this.totalButtons; i++) {
			addButtonConfig(i);
		}
	}

	/**
	 * Adds the specified button config folder. Each folder contains a button.txt
	 * specifying the button's text, audio file, and color.
	 * 
	 * @param i The button number.
	 */
	public void addButtonConfig(int i) {
		String dir = talkboxPath.concat(FileIO.SEP + "button_config_").concat(String.valueOf(i));
		new File(dir).mkdir();
		new File(dir.concat(FileIO.SEP + "sound")).mkdir();
		new File(dir.concat(FileIO.SEP + "image")).mkdir();
		// create new button configuration: this creates the text file with the
		// associated text, color, and sound.
		ButtonConfiguration b = new ButtonConfiguration("Button " + i, null, null, new File(dir));
		// add button config to the array
		this.buttonConfigs[i] = b;
		// write the text file
		b.writeButtonTxt();
	}

	/**
	 * Sets the number of total button configuration folders. If there are already
	 * buttons in the directory, only the remaining ones will be added.
	 * @param buttons The new number of buttons.
	 */
	private void setTotalButtons(int buttons) {
		for (int i = this.totalButtons; i < buttons; i++) {
			this.addButtonConfig(i);
		}
		this.totalButtons = buttons;
	}

	/**
	 * Add an audio set (6 buttons a set) to the Talkbox. A set can be swapped out
	 * for another set at any time.
	 * 
	 */
	public void addAudioSet() {
		// update array size
		int newSize = this.totalButtons + 6;
		ButtonConfiguration[] newConfigs = new ButtonConfiguration[newSize];
		for (int i = 0; i < this.totalButtons; i++) {
			newConfigs[i] = this.buttonConfigs[i];
		}
		this.buttonConfigs = newConfigs;
		// change add file configs
		this.setTotalButtons(newSize);
		this.getNumberOfAudioButtons();
		this.audioSets++;
		this.serializeConfig();
	}

	/**
	 * Returns the button configuration objects.
	 */
	public ButtonConfiguration[] getButtonConfigs() {
		return this.buttonConfigs;
	}

	/**
	 * Returns the path to the current Talkbox configuration directory.
	 * 
	 * @return The string path to the Talkbox configuration directory.
	 */
	public String getConfigDir() {
		return this.talkboxPath;
	}

	/**
	 * Returns the number of active buttons that when pressed will play an audio
	 * file.
	 * 
	 * @return int The total number of active buttons.
	 */
	@Override
	public int getNumberOfAudioButtons() {
		int buttons = 0;
		FilenameFilter configDirs = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return (name.contains("button_config_"));
			}
		};
		for (File f : new File(this.getConfigDir()).listFiles(configDirs)) {
			// checking whether button.txt has a sound file
			if ((FileIO.readTextFile(new File(f.getPath() + FileIO.SEP + "button.txt"))[2]).equals("1")) {
				buttons++;
			}
		}
		return buttons;
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
		return Paths.get(talkboxPath);
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
		String[][] audioFileNames = new String[this.audioSets][6];
		System.out.println(this.audioSets);
		System.out.println(this.getNumberOfAudioButtons());
		// sound folder for each button
		File soundFolder;
		// get WAVE files for each button
		for (int i = 0; i < this.audioSets; i++) {
			for (int j = i * 6; j < (i * 6) + 6; j++) {
				soundFolder = new File(talkboxPath + FileIO.SEP + "button_config_" + j + FileIO.SEP + "sound");
				File[] sounds = soundFolder.listFiles();
				if (sounds.length > 0) {
					audioFileNames[i][j] = sounds[0].getName();
				}
				else {
					audioFileNames[i][j] = "NO SOUND";
				}
			}
		}
		return audioFileNames;
	}

	/**
	 * Removes the i-th (1 .. n) audio set's button_config_ folders. Renames the remaining
	 * configuration folders to continue from the previous ones.
	 * 
	 * @param audioSet The audioset to remove.
	 */
	public void removeAudioSet(int audioSet) {
		// start from the first button of the i-th audio set
		int startButton = (audioSet - 1) * 6;
		
		for (int j = startButton; j < this.totalButtons; j++) {
			// removing the audio set
			if (j < startButton + 6) {
				FileIO.deleteFolder(this.buttonConfigs[j].returnDir());
			}
			//renaming button config folders and changing text files
			else {
				File buttonConfig = this.buttonConfigs[j].returnDir();
				File newDir = new File(buttonConfig.getParent() + FileIO.SEP + "button_config_" + (j - 6));
				boolean success = buttonConfig.renameTo(newDir);
				if(success == false) {
					throw new RuntimeException("NOT WORKING");
				}
			}
			if(this.totalButtons - j > 6) {
				this.buttonConfigs[j] = this.buttonConfigs[j + 6];
			}
		}
		for(int i = startButton + 6;i < totalButtons - 6;i++) {
			this.buttonConfigs[i] = this.buttonConfigs[i+6];
		}
		this.totalButtons -= 6;
		this.audioSets--;
		this.serializeConfig();
		
		//Updating the array of button configs
		ButtonConfiguration [] newArray = new ButtonConfiguration[this.totalButtons];
		for(int i = 0;i < newArray.length;i++) {
			newArray[i] = this.buttonConfigs[i];
		}
		this.buttonConfigs = newArray;
	}
}
