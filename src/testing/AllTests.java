package testing;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import filehandler.FileIO;

@RunWith(Suite.class)
@SuiteClasses({ ConfigSerializationTest.class, FileIOTest.class})
public class AllTests {
	@BeforeAll
	void createTestDir() {
		new File(Directories.HOME + "talkboxtest").mkdirs();
	}
}
