package filehandler;

import static java.nio.file.StandardCopyOption.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIO {
	/**
	 * Puts file into folder specified by path.
	 * @param file The file of .wav and .aiff format
	 * @param dir The new path in backslashes from the root
	 */
	public static void addFile(File toWrite, String dir) {
		try {
			File newFile = new File(toWrite, dir);
			FileOutputStream fileStream = new FileOutputStream(newFile) ;
			DataOutputStream dataStream = new DataOutputStream(fileStream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Puts file into folder specified by path.
	 * @param filePath The absolute path of file in string format
	 * @param dir The absolute path of destination in string format
	 */
	public static void addFile(String filePath, String dir) {
		try {
			Path orig = Paths.get(filePath);
			Path dest = Paths.get(dir);
			Files.move(orig, dest, ATOMIC_MOVE);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InvalidPathException e) {
			e.printStackTrace();
		}
	}
}