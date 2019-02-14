package testing;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import filehandler.FileIO;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileIOTest {
	private static String RESOURCE = "src" + FileIO.SEP + "testing" + FileIO.SEP;
	private static File[] FILES = new File[] { new File(RESOURCE + "test.wav"), new File(RESOURCE + "notwave.m4a"),
			new File(RESOURCE + "image.png"), new File(RESOURCE + "harbin.jpg") };
				
    @BeforeClass
	public static void setUp() {
		// creating directory for testing copy methods
		new File(Directories.TESTING + "copy").mkdirs();
		// add test files
		for (File f : FILES) {
			FileIO.copyFile(f.getAbsolutePath(), Directories.TESTING + f.getName());
		}
		
	}
	@Test
	public void testWaveFormat() {
		// testing wave file
		assertTrue(FileIO.checkWaveFormat(FILES[0]));
		assertTrue(FileIO.checkFileFormatByPath(Directories.TESTING + "test.wav"));
		// testing .m4a file
		assertFalse(FileIO.checkWaveFormat(FILES[1]));
		assertFalse(FileIO.checkFileFormatByPath(Directories.TESTING + "notwave.wav"));
	}

	@Test
	public void testCopyFile() {
		// copy by directory
		String dest = Directories.TESTING + "copy";
		FileIO.copyFile(Directories.TESTING + "test.wav", dest + FileIO.SEP + "test.wav");
		assertTrue((new File(dest + FileIO.SEP + "test.wav")).exists());
		// copy by file
		new File(Directories.TESTING + "copytwo").mkdirs();
		FileIO.copyFile(new File(Directories.TESTING + "test.wav"), new File(Directories.TESTING + "copytwo" + FileIO.SEP + "test.wav"));
		assertTrue(new File(Directories.TESTING + "copytwo" + FileIO.SEP + "test.wav").exists());
		// file does not exist: should not move anything 
		
		//TODO prevent the IO exception being thrown
		FileIO.copyFile(Directories.TESTING + "FGGGAG", dest + FileIO.SEP + "FGGGAG");
		assertFalse(new File(dest.concat(FileIO.SEP + "FGGGAG")).exists());
		// dest does not exist: should not move anything
		dest = "dest" + FileIO.SEP + "gaga";
		FileIO.copyFile(Directories.TESTING +  "test.wav", dest + "test.wav");
		assertFalse(new File(dest + FileIO.SEP + "test.wav").exists());
	}

	@Test
	public void testWriteToText() {
		File textPath = new File(Directories.TESTING + "create.txt");
		FileIO.createTextFile(textPath, "testing123");
		String[] f = FileIO.readTextFile(textPath);
		assertTrue(f[0].equals("testing123"));
	}
	
	@AfterEach
	public static void tearDown() {
		new File(Directories.TESTING + FileIO.SEP + "copy").delete();
		new File(Directories.TESTING + FileIO.SEP + "copytwo").delete();
	}
}
