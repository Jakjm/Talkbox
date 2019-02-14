package testing;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ConfigSerializationTest.class, FileIOTest.class, MusicTest.class})
public class AllTests {
	@BeforeAll
	void createTestDir() {
		new File(Directories.HOME + "talkboxtest").mkdirs();
	}
}
