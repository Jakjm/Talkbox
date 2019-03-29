package externalTests;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;


import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;



import main.java.Talkbox.configurer.ButtonConfiguration;
import main.java.Talkbox.configurer.Configuration;
import main.java.Talkbox.filehandler.FileIO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationTest {
	public static Configuration cf;

	@BeforeAll
	public static void setUp() {
		new File("test").mkdirs();
		cf = new Configuration("test");
	}

	@Test
	public void testA() {
		// default number is 6
		assertTrue(cf.getTotalNumberOfButtons() == 6);
		assertTrue(cf.getNumberOfAudioButtons() == 0);
		File tbDir = new File(cf.getConfigDir());
		// see if button configuration folders are set
		for (int i = 0; i < 6; i++) {
			assertTrue(new File(tbDir + FileIO.SEP + "button_config_" + i).exists());
		}
		assertTrue(new File(tbDir + FileIO.SEP + "serialized_config").exists());
		// add a new audio set
		cf.addAudioSet();
	}

	@Test
	public void testB() {
		assertTrue(cf.getTotalNumberOfButtons() == 12);
		assertEquals(cf.getConfigDir(), "test" + FileIO.SEP + "TalkboxData");
		// test the ability to read from a .tbc configuration file
		Configuration p = Configuration.readConfiguration(new File("test" + FileIO.SEP + "TalkboxData"));
		assertTrue(p.getNumberOfAudioSets() == cf.getNumberOfAudioSets());
		assertTrue(p.getNumberOfAudioSets() == 2);
		assertTrue(p.getNumberOfAudioButtons() == cf.getNumberOfAudioButtons());
		assertTrue(p.getTotalNumberOfButtons() == 12);
		p.addAudioSet();
		assertTrue(p.getTotalNumberOfButtons() == 18);
		// adding button sounds
		File sound = new File("src" + FileIO.SEP + "test" + FileIO.SEP + "resources" + "test.wav");
		p.getButtonConfigs()[0].addSoundFile(sound);
		p.getButtonConfigs()[5].addSoundFile(sound);
		assertTrue(p.getNumberOfAudioButtons() == 2);
		Configuration f = Configuration.readConfiguration(new File("test" + FileIO.SEP + "TalkboxData"));
		f.removeAudioSet(2);
		assertTrue(f.getTotalNumberOfButtons() == 12);
		assertTrue(f.getRelativePathToAudioFiles().toString().equals("test" + FileIO.SEP + "TalkboxData"));
	}
	
	@Test
	public void testC() {
		String newTb = "test" + FileIO.SEP + "Talkboxtest";
		new File(newTb).mkdir();
		Configuration p = new Configuration(newTb);
		ButtonConfiguration[] bc = p.getButtonConfigs();
		bc[0].addSoundFile(new File("src/test/resources/test.wav"));
		bc[1].addSoundFile(new File("src/test/resources/test.wav"));
		String[][] audioSets = p.getAudioFileNames();
		for (String[] audioSet : audioSets) {
			int count = 0;
			System.out.println(Arrays.toString(audioSet));
			for (String name : audioSet) {
				if (count < 2) {
					assertTrue(name.equals("sound.wav"));
				}
				else {
					assertTrue(name.equals("NO SOUND"));
				}
				count++;
			}
		}
		ArrayList<String> arr = FileIO.readTextFile(new File(bc[2].returnDir().getPath() + "/button.txt"));
		bc[2].addImageFile(new File("src/test/resources/harbin.jpg"));
		arr = FileIO.readTextFile(new File(bc[2].returnDir().getPath() + "/button.txt"));
		assertTrue(arr.get(3).equals("1"));
		FileIO.deleteFolder(new File("test"));
	}
}
