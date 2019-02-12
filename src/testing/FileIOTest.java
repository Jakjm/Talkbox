package testing;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import filehandler.FileIO;

class FileIOTest {
	private String homeDir;
	private String testingDir;
	private static File[] sampleFiles = new File[] { new File("notwave.m4a"), new File("test.wav"), new File("notwave.m4a"),
			new File("image.png"), new File("harbin.jpg") };

	@BeforeClass
	void setUp() {
		// create testing directory
		homeDir = System.getProperty("home");
		File x = new File(homeDir + FileIO.SEP + "test");
		testingDir = x.getPath();
		// creating directory for testing copy methods
		new File(testingDir + FileIO.SEP + "copy");
		// add test files
		for (File f : sampleFiles) {
			FileIO.copyFile(f, testingDir);
		}
	}
	@AfterClass
	void tearDown() {
//		new File(testingDir).delete();
	}

	@Test
	void testWaveFormat() {
		// testing wave file
		assertTrue(FileIO.checkWaveFormat(sampleFiles[1]));
		assertTrue(FileIO.checkFileFormatByPath(sampleFiles[1].getPath()));
		// testing .m4a file
		assertFalse(FileIO.checkWaveFormat(sampleFiles[2]));
		assertFalse(FileIO.checkFileFormatByPath(sampleFiles[2].getPath()));
	}

	@Test
	void testCopyFile() {
		// file move
		String dest = testingDir + FileIO.SEP + "copy";
		FileIO.copyFile(testingDir + "test.wav", dest);
		assertTrue((new File(dest + FileIO.SEP + "test.wav")).exists());
		// file does not exist: should not move anything
		FileIO.copyFile(testingDir + FileIO.SEP + "FGGGAG", dest);
		assertFalse(new File(dest.concat(FileIO.SEP + "FGGGAG")).exists());
		// dest does not exist: should not move anything
		dest = "dest" + FileIO.SEP + "gaga";
		FileIO.copyFile(testingDir + "test.wav", dest);
		assertFalse(new File(dest + FileIO.SEP + "test.wav").exists());
	}

	@Test
	void testCheckImageFile() {
		// check if png is verified
		assertTrue(FileIO.checkImageFile(sampleFiles[2].getPath()));
		// check if jpg is accepted
		assertTrue(FileIO.checkImageFile(sampleFiles[3].getPath()));
		// check if wave file is rejected
		assertFalse(FileIO.checkImageFile("test.wav"));
	}

	@Test
	void testWriteToText() {
		File textPath = new File(testingDir + FileIO.SEP + "create.txt");
		FileIO.createTextFile(textPath, "testing123");
		FileIO.readTextFile(textPath);
	}
}
