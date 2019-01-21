package configurer;

import java.nio.file.Path;

import talkbox.TalkBoxConfiguration;

/**
 * Configuration for Talkbox interface. Utilities include
 * setting buttons, librayr path, and its audio files.
 *
 */
public class Configuration implements TalkBoxConfiguration {

	@Override
	public int getNumberOfAudioButtons() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfAudioSets() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalNumberOfButtons() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Path getRelativePathToAudioFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getAudioFileNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
