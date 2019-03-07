package externalTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;

import main.java.Talkbox.filehandler.FileIO;
import main.java.Talkbox.musicplayer.MusicPlayer;
import main.java.Talkbox.recording.MusicRecorder;

public class MusicRecorderTest {

	@Test
	public void test() {
		new File("test").mkdir();
		MusicRecorder g = new MusicRecorder();
		g.record();
		//Waiting five seconds
		long currentTime = System.currentTimeMillis() + 5200;
		while(System.currentTimeMillis() < currentTime) {
			
		}
		File testFile = new File("test/rec.wav");
		MusicRecorder.writeToFile(g.stop(), g.getFormat(), testFile);
		MusicPlayer p = new MusicPlayer(testFile);
		assertEquals("0:05", p.getTrackLength());
		FileIO.deleteFolder(new File("test"));
	}

}
