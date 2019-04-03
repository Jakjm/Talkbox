package externalTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

import main.java.Talkbox.browsing.FileSelector;
import main.java.Talkbox.browsing.SelectionListener;
import main.java.Talkbox.filehandler.FileIO;

/**
 * JUnit test for the FileSelector class.
 * 
 * @author jordan
 * @version February 20th 2019
 */
public class SelectorTest {
	public boolean selectedFile = false;
	public File textFile = null;

	@Test
	public void test1() {
		FileSelector selector = null;
		File testDirectory = null;

		// Creating fileSelector and files for the test.
		try {
			selector = new FileSelector(null, FileSelector.TEXT);
			selector.setVisible(true);
			testDirectory = new File("selectorTest");
			testDirectory.mkdir();
			textFile = new File("selectorTest" + FileSelector.fileSep + "a.txt");
			PrintWriter printer = new PrintWriter(textFile);
		} catch (FileNotFoundException e) {
			fail("This should not cause any errors.");
		}

		// Testing the openDirectory method of the file selector.
		try {
			selector.openDirectory(testDirectory);
		} catch (Exception e) {
			fail("This should cause no errors");
		}

		// Creating a selectionListener for the fileSelector.
		selector.setSelectionListener(new SelectionListener() {
			public void onFileSelected(File file) {
				assertEquals(file, textFile);
				selectedFile = true;
			}
		});
		selector.select(textFile);
		long delay = System.currentTimeMillis() + 100;
		while (System.currentTimeMillis() < delay) {
		}
		assertSame(selectedFile, true);

		// Removing the testing directories.
		textFile.delete();
		FileIO.deleteFolder(testDirectory);
		selector.setVisible(false);
	}

}
