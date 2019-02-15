package testing;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import filehandler.FileIO;

/**
 * Tests for FileIO, ConfigSerialization, and Configuration.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ ConfigSerializationTest.class, FileIOTest.class, ConfigurationTest.class })
public class AllTests {
	public static String HOME = System.getProperty("user.home") + FileIO.SEP;
	public static String TESTING = HOME + "talkboxtest";

	@BeforeClass
	public static void createTestDir() {
		new File(HOME + "talkboxtest").mkdirs();
	}

	@AfterClass
	public static void tearDown() {
		FileIO.deleteFolder(new File(TESTING));
	}
}
