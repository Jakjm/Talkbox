package testing;

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
		// recording for 5 seconds
		try {
			Thread.sleep(5000);
		}
		catch (InterruptedException e) {
		}
		File testFile = new File("test/test.wav");
		MusicRecorder.writeToFile(g.stop(), g.getFormat(), testFile);
		MusicPlayer p = new MusicPlayer(testFile);
		assertEquals("0:04", p.getTrackLength());
	}

}
