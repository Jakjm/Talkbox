package externalTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import main.java.Talkbox.filehandler.FileIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileIOTest {
	public static String TESTING = "test" + FileIO.SEP + "talkboxtest";
	private static String RESOURCE = "src" + FileIO.SEP + "test" + FileIO.SEP + "resources" + FileIO.SEP;
	private static File[] FILES = new File[] { new File(RESOURCE + "test.wav"), new File(RESOURCE + "notwave.m4a"),
			new File(RESOURCE + "image.png"), new File(RESOURCE + "harbin.jpg") };

	@BeforeAll
	public static void setUp() {
		// creating directory for testing copy methods
		new File(TESTING + FileIO.SEP + "copy").mkdirs();
		// add test files
		for (File f : FILES) {
			FileIO.copyFile(f.getAbsolutePath(), TESTING + FileIO.SEP + f.getName());
		}

	}

	@Test
	public void testA() {
		// testing wave file
		assertTrue(FileIO.checkWaveFormat(FILES[0]));
		assertTrue(FileIO.checkFileFormatByPath(TESTING + FileIO.SEP + "test.wav"));
		// testing .m4a file
		assertFalse(FileIO.checkWaveFormat(FILES[1]));
		assertFalse(FileIO.checkFileFormatByPath(TESTING + FileIO.SEP + "notwave.wav"));
	}

	@Test
	public void testB() {
		// copy by directory
		String dest = TESTING + FileIO.SEP + "copy";
		FileIO.copyFile(TESTING + FileIO.SEP + "test.wav", dest + FileIO.SEP + "test.wav");
		assertTrue((new File(dest + FileIO.SEP + "test.wav")).exists());
		// copy by file
		new File(TESTING + FileIO.SEP + "copytwo").mkdirs();
		FileIO.copyFile(new File(TESTING + FileIO.SEP + "test.wav"),
				new File(TESTING + FileIO.SEP + "copytwo" + FileIO.SEP + "test.wav"));
		assertTrue(new File(TESTING + FileIO.SEP + "copytwo" + FileIO.SEP + "test.wav").exists());
		// file does not exist: should not move anything
		FileIO.copyFile(TESTING + FileIO.SEP + "FGGGAG", dest + FileIO.SEP + "FGGGAG");
		assertFalse(new File(dest.concat(FileIO.SEP + "FGGGAG")).exists());
		// dest does not exist: should not move anything
		dest = "dest" + FileIO.SEP + "gaga";
		FileIO.copyFile(TESTING + FileIO.SEP + "test.wav", dest + FileIO.SEP + "test.wav");
		assertFalse(new File(dest + FileIO.SEP + "test.wav").exists());
	}

	@Test
	public void testC() {
		File textPath = new File(TESTING + FileIO.SEP + "create.txt");
		FileIO.textToFile(textPath, "testing123");
		ArrayList<String> f = FileIO.readTextFile(textPath);
		assertTrue(f.get(0).equals("testing123"));
	}

	@Test
	public void testD() {
		File harbin = new File("src/test/resources/harbin.jpg");
		File notImage = new File("src/test/resources/notwave.m4a");
		assertTrue(FileIO.getExt(notImage).equals(".m4a"));
		assertTrue(FileIO.checkImageFile(harbin));
		assertFalse(FileIO.checkImageFile(notImage));
	}

	@Test
	public void testE() {
		File txtTest = new File("test" + FileIO.SEP + "testReplace.txt");
		FileIO.textToFile(txtTest, "erectus" + '\n' + "sapiens" + '\n' + "habilis" + '\n' + "pan");
		FileIO.editTextLine(txtTest, "neanderthal", 2);
		assertTrue(FileIO.readTextFile(txtTest).get(2).equals("neanderthal"));
		System.out.println(FileIO.readTextFile(txtTest));
		FileIO.textToFile(txtTest, "");
		assertTrue(FileIO.readTextFile(txtTest).size() == 0);
		FileIO.deleteFolder(new File("test"));
	}
}
