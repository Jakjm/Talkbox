package externalTests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import main.java.Talkbox.configurer.ConfigSerialization;
import main.java.Talkbox.configurer.Configuration;
import main.java.Talkbox.filehandler.FileIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigSerializationTest {

	@Test
	public void testA() {
		new File("test").mkdirs();
		Configuration cf = new Configuration("test");
		assertTrue(cf.getNumberOfAudioButtons() == 0);
		cf.addAudioSet();
		assertTrue(cf.getTotalNumberOfButtons() == 12);
		// deserialize
		Configuration p = ConfigSerialization.deserialize("test" + FileIO.SEP + "TalkboxData" + FileIO.SEP + "serialized_config" + FileIO.SEP + "config.tbc");
		assertTrue(p.getTotalNumberOfButtons() == 12);
		FileIO.deleteFolder(new File(cf.getConfigDir()));
		new File("test").mkdirs();
		FileIO.deleteFolder(new File("test"));
	}
}
