package filehandler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

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

}
