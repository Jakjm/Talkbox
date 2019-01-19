package recording;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
	public static void main (String[] args) {
		File f = new File("C:/Users/Rohan/Documents/testing.wav");
		MusicRecorder recorder = new MusicRecorder();
		 
        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.stop();
            }
        });
        stopper.start();
        recorder.record(f);
	}
}
