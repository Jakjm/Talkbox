package filehandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
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
	public static final String WAVFORMAT = ".wav";
	public static final String WAVEFORMAT = ".wave";
	/**
	 * Verifies if file format is WAVE by checking its path.
	 * @param path - The absolute path of the file in string form.
	 * @return true iff the file format is WAVE.
	 */
	public static boolean checkFileFormatByPath(String path)  {
		boolean isWave = false;
		// if the extension is .wav we continue to check its format
		String ext = path.substring(path.lastIndexOf("."));
		if (ext.equals(WAVEFORMAT) || ext.equals(WAVFORMAT)) {
			File toCheck = new File(path);
			isWave = checkFileFormat(toCheck);
		}
		return isWave;
		
	}
	/**
	 * Verifies if a file format is WAVE by checking its format.
	 * @param file - the file to be verified
	 * @return true iff the file format is WAVE.
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
	 * Moves file specified by directory into a path. If path is to a file,
	 * the parent directory is taken. 
	 * @param File - The target file to be moved.
	 * @param dir - The path of destination folder.
	 */
	public static void moveFile(File file, String dir) {
		String filePath = file.getPath();
		moveFile(filePath, dir);
	}
	/**
	 * Moves file specified by directory into a path. If path is to a file,
	 * the parent directory is taken. 
	 * @param origin - The path of the target file.
	 * @param destination - The path of destination folder.
	 */
	public static void moveFile(String origin, String destination) {
		try {
			// if destination is not directory then take parent directory
			System.out.println(new File(destination).isFile());
			if (new File(destination).isFile()) {
				destination = destination.substring(0, destination.lastIndexOf("\\"));
			}
			// construct paths
			Path origPath = Paths.get(origin);
			Path destPath = Paths.get(destination);
			// copy file into destination
			Files.copy(origPath, destPath.resolve(origPath.getFileName()));
		}
		catch (InvalidPathException | IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Verifies if file format is an image.
	 * Intended to accept PNG and JPEG files.
	 * @param path - The path image file.
	 * @return true if the file is an image.
	 */
	public static boolean checkImageFile(String path) {
		boolean isImg = false;
		try {
			// see if Image IO can read the image
			File imgFile = new File(path);
			// null check
			isImg = ImageIO.read(imgFile) != null;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return isImg;
	}
	/**
	 * Verifies if file format is an image.
	 * Intended to accept PNG and JPEG files.
	 * @param path - The image file
	 * @return true if the file is an image
	 */
	public static boolean checkImageFile(File file) {
		return checkImageFile(file.getPath());
	}
}
