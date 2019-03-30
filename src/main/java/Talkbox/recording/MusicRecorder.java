package main.java.Talkbox.recording;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Rewritten Audio Recording Class
 * @author jordan
 * @version January 20th 2019
 */
public class MusicRecorder {
	private volatile TargetDataLine line;
	private final AudioFormat format;
	private static final int NOT_RECORDING = 0;
	private static final int RECORDING = 1;
	private volatile int state = NOT_RECORDING;
	private volatile ByteArrayOutputStream stream;
	private Thread recordingThread;

	/**
	 * Constructor for MusicRecorder
	 */
	public MusicRecorder() {
		try {
			line = AudioSystem.getTargetDataLine(null);
			line.open(line.getFormat());
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		format = line.getFormat();
	}

	/**
	 * Starts recording and writes to specified file.
	 */
	public void record() {
		if (this.state == RECORDING) {
			return;
		}
		this.state = RECORDING;
		recordingThread = new Thread(new ThreadTask());
		recordingThread.start();
	}

	/**
	 * Stop sound recording.
	 * 
	 * @return ByteArrayOutputStream - the stream that the sound has been recorded
	 *         to, or null if not recording.
	 */
	public ByteArrayOutputStream stop() {
		if (this.state == NOT_RECORDING) {
			return null;
		}
		this.state = NOT_RECORDING;
		try {
			recordingThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return stream;
	}
	
	/**
	 * Method for writing an audio Byte Array Stream to a file. 
	 * @param format
	 * @param file
	 */
	public static void writeToFile(ByteArrayOutputStream stream, AudioFormat format, File file) {
		try {
			byte[] rawData = stream.toByteArray();
			ByteArrayInputStream inStream = new ByteArrayInputStream(rawData);
			AudioInputStream ais = new AudioInputStream(inStream, format, rawData.length / format.getFrameSize());
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Returns whether the music recorder is currently recording.
	 */
	public boolean isRecording() {
		return this.state == RECORDING;
	}
	/**
	 * @return the AudioFormat of the TargetDataline for this recorder.
	 */
	public AudioFormat getFormat() {
		return this.format;
	}

	/** Task for continuously recording **/
	private class ThreadTask implements Runnable {
		public void run() {
			stream = new ByteArrayOutputStream();
			byte[] data = new byte[line.getBufferSize() / 5];
			int bytesRead;
			line.start();

			// Flush the line to get rid of leftover data
			line.flush();
			// Recording sound into stream
			while (state == RECORDING) {
				bytesRead = line.read(data, 0, data.length);
				stream.write(data, 0, bytesRead);
			}
			// Flushing remaining data
			line.stop();
			bytesRead = line.read(data, 0, data.length);
			stream.write(data, 0, bytesRead);
			line.drain();
		}
	}
}