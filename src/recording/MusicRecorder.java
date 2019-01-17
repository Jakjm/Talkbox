package recording;

import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class MusicRecorder {
	AudioFormat format;
	volatile TargetDataLine targetDataLine;
	volatile File currentFile;
	private Thread writeThread;
	/**
	 * Constructor for MusicRecorder
	 */
	public MusicRecorder() {
		try {
			targetDataLine = AudioSystem.getTargetDataLine(null);
			targetDataLine.open();
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Starts recording and writes to specified file. 
	 * @param file - the target file in .wav format
	 */
	public void record(File file) {
		this.currentFile = file;
		targetDataLine.start();
		writeThread = new Thread(new Runnable() {
			public void run() {
				try{
					AudioSystem.write(new AudioInputStream(targetDataLine),AudioFileFormat.Type.WAVE,currentFile);
				}
				catch (Exception e){
					
				}
			}
		});
		writeThread.start();
	}
	/**
	 * Stop sound recording. 
	 */
	public void stop() {
		// close and stop data line
		Thread thread = new Thread(new Runnable() {
			public void run() {
				targetDataLine.stop();
				targetDataLine.drain();
				targetDataLine.close();
			}
		});
		thread.start();
		try {
			writeThread.join(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
