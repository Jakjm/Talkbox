package filehandler;

import static java.nio.file.StandardCopyOption.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class FileIO {
	/**
	 * Utilities for audio file handling.
	 * @author Alberto Mastrofrancesco
	 * @author Jordan Malek
	 * @author Rohan Talkad
	 * @version 1.0
	 */
	
	/**
	 * Verifies if file format is WAVE by checking its path.
	 * @param path - The absolute path in string form
	 * @return whether the file format is WAVE
	 */
	public static boolean checkFileFormatByPath(String path)  {
		boolean isWave = false;
		// if the extension is .wav we continue to check its format
		String ext = path.substring(path.lastIndexOf(".") + 1);
		if (ext.equals("wav")) {
			File toCheck = new File(path);
			isWave = FileIO.checkFileFormat(toCheck);
		}
		return isWave;
		
	}
	/**
	 * Verifies if a file format is WAVE by checking its format.
	 * @param file - the file to be verified
	 * @return whether the file format is WAVE
	 */
	public static boolean checkFileFormat(File file) {
		boolean isWave = false;
		try {
			// return file format type from the audio input stream of the file
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			isWave = fileFormat.getType().equals(AudioFileFormat.Type.WAVE);
		}
		catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return isWave;
	}
	/**
	 * Puts file into folder specified by path.
	 * @param File The target file to be moved.
	 * @param dir The absolute path of destination in string format
	 */
	public static void addFile(File file, String dir) {
		try {
			Path orig = Paths.get(file.getPath());
			Path dest = Paths.get(dir);
			Files.move(orig, dest, ATOMIC_MOVE);
		}
		catch ( IOException e) {
			e.printStackTrace();
		}
	}
}