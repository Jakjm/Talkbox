package filehandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class FileIO {
	/**
	 * Utilities for handling files. Common uses include checking audio file format,
	 * reading/writing to text files, copying files, etc.
	 * 
	 * @author Alberto Mastrofrancesco
	 * @author Jordan Malek
	 * @author Rohan Talkad
	 */
	public static final String WAVFORMAT = ".wav";
	public static final String WAVEFORMAT = ".wave";
	public static final String SEP = System.getProperty("file.separator");

	/**
	 * Verifies if file format is WAVE by checking its path.
	 * 
	 * @param path The absolute path of the file.
	 * @return true iff the file format is WAVE.
	 */
	public static boolean checkFileFormatByPath(String path) {
		boolean isWave = false;
		// if the extension is .wav we continue to check its format
		String ext = path.substring(path.lastIndexOf("."));
		if (ext.equals(WAVEFORMAT) || ext.equals(WAVFORMAT)) {
			File toCheck = new File(path);
			isWave = checkWaveFormat(toCheck);
		}
		return isWave;

	}

	/**
	 * Verifies if a file format is WAVE by checking its format. TODO rename check
	 * wave format or something similar
	 * 
	 * @param file - the file to be verified
	 * @return true iff the file format is WAVE.
	 */
	public static boolean checkWaveFormat(File file) {
		boolean isWave = false;
		try {
			// return file format type from the audio input stream of the file
			AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
			isWave = fileFormat.getType().equals(AudioFileFormat.Type.WAVE);
		} catch (IOException | UnsupportedAudioFileException e) {
		}
		return isWave;
	}

	/**
	 * Moves file specified by directory into a path.
	 * 
	 * @param file The path of the target file.
	 * @param dest The path of the destination folder.
	 */
	public static void copyFile(File file, File dest) {
		String filePath = file.getAbsolutePath();
		String destPath = dest.getAbsolutePath();
		copyFile(filePath, destPath);
	}

	/**
	 * Moves file specified by directory into a path.
	 * 
	 * @param file The path of the target file.
	 * @param dest The path of the destination folder.
	 */
	public static void copyFile(String filePath, String destPath) {
		try {
			Path file = Paths.get(filePath);
			Path dest = Paths.get(destPath);
			// copy file into destination
			Files.copy(file, dest, REPLACE_EXISTING);
		} catch (InvalidPathException | IOException e) {
		}
	}

	/**
	 * Creates and writes content to a text file.
	 * 
	 * @param toWrite The file to write to.
	 * @param content The string to write.
	 */
	public static void createTextFile(File toWrite, String content) {
		BufferedWriter bf;
		try {
			bf = new BufferedWriter(new FileWriter(toWrite));
			bf.write(content);
			bf.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Reads the specified text file and returns an array of each line.
	 * @param toRead The file to read.
	 * @return The array of lines.
	 */
	public static String[] readTextFile(File toRead) {
		String[] data = new String[3];
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(toRead))) {
			int i = 0;
			while ((line = br.readLine()) != null) {
				data[i++] = line;
			}
		} catch (IOException e) {
		}
		return data;

	}

	/**
	 * Deletes the specified folder.
	 * 
	 * @param folder the Folder to delete
	 */
	public static void deleteFolder(File folder) {
		for (File x : folder.listFiles()) {
			if (x.isFile()) {
				x.delete();
			} else {
				deleteFolder(x);
			}
		}
		folder.delete();
	}

}
