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
	public Configuration cf;
	
	@Test
	public void testSerialize() {
		Configuration cf = new Configuration(Directories.TESTING);
		ConfigSerialization.serialize(Directories.TESTING, cf);
		assertTrue(new File(Directories.TESTING + FileIO.SEP + "talkboxData" + FileIO.SEP + "serialized_config" + FileIO.SEP + "config.tbc").exists());
	}
}
