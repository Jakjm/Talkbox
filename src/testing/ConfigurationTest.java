package testing;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import configurer.Configuration;
import filehandler.FileIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationTest {
	public static String HOME = System.getProperty("user.home") + FileIO.SEP;
	public static String TESTING = HOME + "talkboxtest";
	public static Configuration cf;

	@BeforeClass
	public static void setUp() {
		cf = new Configuration(TESTING);
	}
	@Test
	public void testButtons()
	{
		// default number is 6
		assertTrue(cf.getNumberOfAudioButtons() == 6);
		File tbDir = new File(cf.getConfigDir());
		for (int i = 1; i < 7; i++) {
			File x = tbDir.listFiles()[i];
			if (!x.getName().equals("button_config_" + (i-1))) {
				fail();
			}
			assertTrue(x.listFiles()[0].getName().equals("serialized_config"));
			if (! (x.listFiles()[0].getName()).equals("button.txt")) {
				fail();
			}
		}
	}
}
