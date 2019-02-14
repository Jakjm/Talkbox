package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import configurer.ConfigSerialization;
import configurer.Configuration;
import filehandler.FileIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigSerializationTest {
	public static String HOME = System.getProperty("user.home") + FileIO.SEP;
	public static String TESTING = HOME + "talkboxtest";
	public static String RESOURCES = "src" + FileIO.SEP + "testing" + FileIO.SEP;

	@Test
	public void testSerialize() {
		Configuration cf = new Configuration(TESTING);
		cf.setActiveButtons(3);
		ConfigSerialization.serialize(TESTING + FileIO.SEP + "test.tbc", cf);
		assertTrue(new File(TESTING + FileIO.SEP + "test.tbc").exists());
		// deserialize
		Configuration p = ConfigSerialization.deserialize(TESTING + FileIO.SEP + "test.tbc");
		assertTrue(p.getNumberOfAudioButtons() == 3);
	}
}
