package recording;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class MusicRecorder {
	AudioFormat format;
	TargetDataLine targetDataline;
	int bufferSize;
	File currentFile;
	/**
	 * Constructor for MusicRecorder
	 */
	public MusicRecorder() {
		try {
			targetDataline = AudioSystem.getTargetDataLine(null);
			targetDataline.open();
			this.bufferSize = targetDataline.getBufferSize();
			
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
	}
	/**
	 * Stop sound recording. 
	 * @throws FileNotFoundException if File does not exist
	 */
	public void stop() throws FileNotFoundException {
		targetDataline.stop();
		if(currentFile == null) {
			throw new FileNotFoundException("File not valid");
		}
		try {
			AudioSystem.write(new AudioInputStream(targetDataline),AudioFileFormat.Type.WAVE,currentFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentFile = null;
	}
}
