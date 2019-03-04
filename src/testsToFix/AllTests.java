package testsToFix;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import main.java.Talkbox.filehandler.FileIO;
import test.java.Talkbox.MusicTest;
import test.java.Talkbox.SelectorTest;

/**
 * Tests for FileIO, ConfigSerialization, and Configuration.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({MusicTest.class,SelectorTest.class, ConfigSerializationTest.class, FileIOTest.class,ConfigurationTest.class, MusicRecorderTest.class })
public class AllTests {

	@BeforeAll
	public static void createTestDir() {
		new File("test").mkdirs();
	}
	
	@AfterAll
	public static void tearDown() {
		FileIO.deleteFolder(new File("test"));
	}
}
