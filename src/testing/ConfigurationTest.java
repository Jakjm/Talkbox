package testing;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

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

	/*
	 * TODO: this isn't going to work - there are more files in the configuration directory. 
	 */
	@Test
	public void testButtons() {
		// default number is 6
		assertTrue(cf.getTotalNumberOfButtons() == 6);
		assertTrue(cf.getNumberOfAudioButtons() == 0);
		File tbDir = new File(cf.getConfigDir());
		HashSet<String> names = new HashSet<>();
		for (int i = 0; i < 6; i++) {
			names.add("button_config_" + i);
		}
		// see if button configuration folders are set
		for (int i = 0; i < 6; i++) {
			File x = tbDir.listFiles()[i];
			if (!names.contains(x.getName())) {
				fail("Button name" + x.getName());
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
		Configuration p = Configuration.readConfiguration(new File(TESTING + FileIO.SEP + "talkboxData"));
		assertTrue(p.getNumberOfAudioSets() == cf.getNumberOfAudioSets());
		assertTrue(p.getNumberOfAudioSets() == 2);
		assertTrue(p.getNumberOfAudioButtons() == cf.getNumberOfAudioButtons());
		assertTrue(p.getTotalNumberOfButtons() == 12);
		p.addAudioSet();
		assertTrue(p.getTotalNumberOfButtons() == 18);
		// adding button sounds
		File sound = new File(TESTING + FileIO.SEP + "test.wav");
		p.getButtonConfigs()[0].addSoundFile(sound);
		p.getButtonConfigs()[5].addSoundFile(sound);
		assertTrue(p.getNumberOfAudioButtons() == 2);
		Configuration f = Configuration.readConfiguration(new File(TESTING + FileIO.SEP + "talkboxData"));
		f.removeAudioset(2);
		assertTrue(f.getTotalNumberOfButtons() == 12);
		FileIO.deleteFolder(new File(cf.getConfigDir()));
	}
}
