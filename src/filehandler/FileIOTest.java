package filehandler;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

class FileIOTest extends FileIO {

	@Test
	void testCheckFileFormat() {
		File newF = new File("C:\\Users\\Rohan\\Documents\\testing.wav");
		assertTrue(FileIO.checkFileFormat(newF));
		assertTrue(FileIO.checkFileFormatByPath("C:\\Users\\Rohan\\Documents\\testing.wav"));
		newF = new File("C:\\Users\\Rohan\\Documents\\sparrow.m4a");
		assertFalse(FileIO.checkFileFormat(newF));
		assertFalse(FileIO.checkFileFormatByPath("C:\\Users\\Rohan\\Documents\\sparrow.m4a"));
		newF = new File("C:\\Users\\Rohan\\Documents\\sparrow.wav.m4a");
		assertFalse(FileIO.checkFileFormat(newF));
		assertFalse(FileIO.checkFileFormatByPath("C:\\Users\\Rohan\\Documents\\sparrow.wav.m4a"));
	}
	@Test
	void testMoveFile() {
		// file move
		String orig = "C:\\Users\\Rohan\\Desktop\\Aus\\recording300.wav";
		String dest = "C:\\Users\\Rohan\\Documents";
		FileIO.moveFile(orig, dest);
		assertTrue(new File(dest.concat("\\recording300.wav")).exists());
		// txt file move
		orig = "C:\\Users\\Rohan\\Documents\\daysof heaven.txt";
		dest = "C:\\Users\\Rohan\\Desktop\\Aus";
		FileIO.moveFile(orig, dest);
		assertTrue(new File(dest.concat("\\daysof heaven.txt")).exists());
		// file does not exist: should not move anything
		orig = "C:\\Users\\Rohan\\Unknown dir\\daysof heaven.txt";
		dest = "C:\\Users\\Rohan\\Downloads";
		FileIO.moveFile(orig, dest);
		assertFalse(new File(dest.concat("\\daysof heaven.txt")).exists());
		// dest does not exist: should not move anything
		orig = "C:\\Users\\Rohan\\Desktop\\Aus\\recording500.wav";
		dest = "C:\\Users\\Rohan\\unknown";
		FileIO.moveFile(orig, dest);
		assertFalse(new File(dest.concat("\\recording500.wav")).exists());
		// attempt to move to file: places in parent instead
		dest = "C:\\Users\\Rohan\\Documents\\recording200.wav";
		FileIO.moveFile(orig, dest);
		assertTrue(new File(dest.concat("C:\\Users\\Rohan\\Documents\\recording500.wav")).exists());
	}
	@Test
	void testCheckImageFile() {
		String orig = "C:\\Users\\Rohan\\Documents\\talkad.jpg";
		assertTrue(FileIO.checkImageFile(orig));
		orig = "C:\\Users\\Rohan\\Documents\\khoisan.png";
		assertTrue(FileIO.checkImageFile(orig));
		orig = "C:\\Users\\Rohan\\Documents\\beard.docx";
		assertFalse(FileIO.checkImageFile(orig));
	}
}