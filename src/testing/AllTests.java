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
@SuiteClasses({MusicTest.class,SelectorTest.class, ConfigSerializationTest.class, FileIOTest.class, 
	ConfigurationTest.class })
public class AllTests {

	@BeforeClass
	public static void createTestDir() {
		new File("test").mkdirs();
	}
	
	@AfterClass
	public static void tearDown() {
		FileIO.deleteFolder(new File("test"));
	}
}
