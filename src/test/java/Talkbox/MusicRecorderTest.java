package test.java.Talkbox;

import static org.junit.Assert.*;

import java.io.File;


import org.junit.Test;

import musicplayer.MusicPlayer;
import recording.MusicRecorder;

public class MusicRecorderTest {

	@Test
	public void test() {
		MusicRecorder g = new MusicRecorder();
		g.record();
		//Waiting five seconds
		long currentTime = System.currentTimeMillis() + 5200;
		while(System.currentTimeMillis() < currentTime) {
			
		}
		File testFile = new File("test/test.wav");
		MusicRecorder.writeToFile(g.stop(), g.getFormat(), testFile);
		MusicPlayer p = new MusicPlayer(testFile);
		assertEquals("0:05", p.getTrackLength());
	}

}
