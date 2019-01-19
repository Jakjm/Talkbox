package filehandler;

import static java.nio.file.StandardCopyOption.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIO {
	/**
	 * Utilities for audio file handling.
	 * @author Alberto Mastrofrancesco
	 * @author Jordan Malek
	 * @author Rohan Talkad
	 * @version 1.0
	 */
	
	/**
	 * Verifies if file is in .wav format
	 * @param file - The file instance to be checked
	 */
	public static boolean checkFileFormat(File file)  {
		String path = file.getPath();
		String ext = path.substring(path.lastIndexOf(".") + 1);
		boolean isWav = ext.equals(".wav");
		return isWav;
	}
	/**
	 * Puts file into folder specified by path.
	 * @param File The target file to be moved
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