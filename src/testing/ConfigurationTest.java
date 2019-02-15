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
	public void testButtons() {
		// default number is 6
		assertTrue(cf.getNumberOfAudioButtons() == 6);
		File tbDir = new File(cf.getConfigDir());
		// see if button configuration folders are set
		for (int i = 1; i < 6; i++) {
			File x = tbDir.listFiles()[i];
			if (!x.getName().equals("button_config_" + i)) {
				fail();
			}
			if (!(x.listFiles()[0].getName()).equals("button.txt")) {
				fail();
			}
		}
		assertTrue(tbDir.listFiles()[6].getName().equals("serialized_config"));
		// add a new audio set
		cf.addAudioSet();
	}

	@Test
	public void testGetters() {
		assertTrue(cf.getTotalNumberOfButtons() == 12);
		assertEquals(cf.getConfigDir(), TESTING + FileIO.SEP + "talkboxData");
		// test the ability to read from a .tbc configuration file
		Configuration p;
		new File(TESTING + FileIO.SEP + "copyOfTBDir").mkdir();
		p = Configuration.readConfiguration(new File(TESTING + FileIO.SEP + "talkboxData"));
		assertTrue(p.getNumberOfAudioSets() == cf.getNumberOfAudioSets());
		assertTrue(p.getNumberOfAudioSets() == 2);
		assertTrue(p.getNumberOfAudioButtons() == cf.getNumberOfAudioButtons());
		assertTrue(p.getNumberOfAudioButtons() == 12);
		p.addAudioSet();
		assertTrue(p.getNumberOfAudioButtons() == 18);
	}
}
