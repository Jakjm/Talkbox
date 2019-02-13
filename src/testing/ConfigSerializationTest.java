package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import configurer.ConfigSerialization;
import configurer.Configuration;
import filehandler.FileIO;

class ConfigSerializationTest {
	public Configuration config;
	
	@BeforeClass
	public void setUp() throws Exception {
		config = new Configuration(Directories.TESTING);
	}
	
	@Test
	void testSerialize() {
		ConfigSerialization.serialize(Directories.TESTING, config);
		assertTrue(new File(Directories.TESTING + FileIO.SEP + "config.tbc").exists());
	}
	@AfterClass
	void tearDown() throws Exception {
		new File(config.getConfigDir()).delete();
	}
}
