package testing;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import filehandler.FileIO;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FileIOTest {
	private static String homeDir = System.getProperty("user.home");
	private static String testingDir = homeDir + FileIO.SEP + "talkboxtest";
	private static File[] sampleFiles = new File[] { new File("test.wav"), new File("notwave.m4a"),
			new File("image.png"), new File("harbin.jpg") };
				
    @Before
	public void setUp() {
		// create testing directory
		new File(testingDir).mkdirs();
		// creating directory for testing copy methods
		new File(testingDir + FileIO.SEP + "copy").mkdirs();
		// add test files
		for (File f : sampleFiles) {
			FileIO.copyFile(f, testingDir);
		}
		
	}
	@After
	public void tearDown() {
		new File(testingDir).delete();
	}
	@Test
	void testWaveFormat() {
		// testing wave file
		assertTrue(FileIO.checkWaveFormat(sampleFiles[0]));
		assertTrue(FileIO.checkFileFormatByPath(testingDir + FileIO.SEP + "test.wav"));
		// testing .m4a file
		assertFalse(FileIO.checkWaveFormat(sampleFiles[1]));
		assertFalse(FileIO.checkFileFormatByPath(testingDir + FileIO.SEP + "notwave.wav"));
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
		assertTrue(FileIO.checkImageFile("image.png"));
		// check if jpg is accepted
		assertTrue(FileIO.checkImageFile(sampleFiles[3].getPath()));
		// check if wave file is rejected
		assertFalse(FileIO.checkImageFile("test.wav"));
	}

	@Test
	void testWriteToText() {
		File textPath = new File(testingDir + FileIO.SEP + "create.txt");
		FileIO.createTextFile(textPath, "testing123");
		String[] f = FileIO.readTextFile(textPath);
		assertTrue(f[0].equals("testing123"));
	}
}
