package recording;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class MusicRecorder {
	AudioFormat format;
	TargetDataLine targetDataline;
	File currentFile;
	/**
	 * Constructor for MusicRecorder
	 */
	public MusicRecorder() {
		try {
			targetDataline = AudioSystem.getTargetDataLine(null);
			targetDataline.open();
			
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
		targetDataline.start();
		// write audio stream to file
		try {
			AudioSystem.write(new AudioInputStream(targetDataline),AudioFileFormat.Type.WAVE,currentFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Stop sound recording. 
	 */
	public void stop() {
		// close and stop data line
		targetDataline.stop();
		targetDataline.close();
	}
}
