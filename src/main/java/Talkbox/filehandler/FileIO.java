package main.java.Talkbox.filehandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import javax.imageio.ImageIO;
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
	public static final String WAV_FORMAT = ".wav";
	public static final String WAVE_FORMAT = ".wave";
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
		if (ext.equals(WAVE_FORMAT) || ext.equals(WAV_FORMAT)) {
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
	 * Copes the file specified by directory into a path.
	 * 
	 * @param file The path of the target file.
	 * @param dest The path of the target file.
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
	 * Writes content to a text file. Each line should be separated by a new line character.
	 * 
	 * @param toWrite The file to write to.
	 * @param content The string to write.
	 */
	public static void textToFile(File toWrite, String content) {
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
	 * @param lines The number of lines to read and return.
	 * @return An ArrayList of lines.
	 */
	public static ArrayList<String> readTextFile(File toRead) {
		ArrayList<String> content = new ArrayList<>();
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(toRead))) {
			while ((line = br.readLine()) != null) {
				content.add(line);
			}
		} catch (IOException e) {
		}
		return content;

	}
	/**
	 * Edit the specified line of the text file. Do not include the new line character.
	 * @param toEdit The text file to edit.
	 * @param newLine The replacement string.
	 * @param lineNumber The line to replace.
	 */
	public static void editTextLine(File toEdit, String newLine, int lineNumber) {
		List<String> content = readTextFile(toEdit);
		content.set(lineNumber, newLine);
		StringBuilder newTxt = new StringBuilder("");
		int i = -1;
		while (++i < content.size() - 1) {
			newTxt.append(content.get(i) + "\n");
		}
		newTxt.append(content.get(i));
		textToFile(toEdit, newTxt.toString());
	}
	

	/**
	 * Deletes the specified folder
	 * @param folder the Folder to delete
	 */
	public static void deleteFolder(File folder) {
		for (File x : folder.listFiles()) {
			if(x.isDirectory()){
				deleteFolder(x);
			}
			else {
				x.delete();
			}
		}
		folder.delete();
	}
	/**
	 * Returns whether a file is an image.
	 * @param imageFile The image file.
	 * @return True if the file is an image.
	 */
	public static boolean checkImageFile(File imageFile) {
		boolean result = false;
		try {
			result = ImageIO.read(imageFile) != null;
		}
		catch (IOException e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * Returns the extension of the file name.
	 * @param file The file to get the extension from.
	 */
	public static String getExt(File file) {
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf("."));
	}
	/**
	 * Return an ArrayList of files with the given pattern.
	 * @param name The pattern name.
	 * @param folder The folder to search.
	 */
	public static ArrayList<File> getAllFiles(String name, File folder) {
		ArrayList<File> files = new ArrayList<>();
		for (File f : folder.listFiles()) {
			if (f.getName().contains(name)) {
				files.add(f);
			}
		}
		return files;
	}
	/**
	 * Deletes the text files in the given folder with the specified pattern.
	 * Example: wipe("CONFIG_LOG", new File(logs)) will reset all logs with
	 * the name "CONFIG_LOG".
	 * @param pattern The string pattern of the file names.
	 * @param folder The folder of logs.
	 */
	public static void wipe(String pattern, File folder) {
		ArrayList<File> files = getAllFiles(pattern, folder);
		for (File f : files) {
			f.delete();
		}
	}
	
	/**
	 * Compresses the text files with the given pattern into the destination folder.
	 * @param pattern The pattern for the file name.
	 * @param folder The folder of text files. 
	 * @param The destination folder.
	 */
	public static void compressText(String pattern, File folder, File destination) {
		ArrayList<File> files = getAllFiles(pattern, folder);
		File destText = new File(destination.getPath() + SEP + pattern + "_Export");
		StringBuilder sb = new StringBuilder("");
		for (File f : files) {
			for (String line : FileIO.readTextFile(f)) {
				sb.append(line + "\n");
			}
		}
		FileIO.textToFile(destText, sb.toString());
	}
}
