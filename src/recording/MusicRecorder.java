package recording;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
	public MusicRecorder() {
		try {
			targetDataline = AudioSystem.getTargetDataLine(null);
			targetDataline.open();
			this.bufferSize = targetDataline.getBufferSize();
			this.targetDataline = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, format));
			
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	public void record(File file) {
		this.currentFile = file;
		targetDataline.start();
	}
	public void stop() {
		targetDataline.stop();
		if(currentFile == null) {
			throw new RuntimeException("File not valid");
		}
		try {
			AudioSystem.write(new AudioInputStream(targetDataline),AudioFileFormat.Type.WAVE,currentFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		currentFile = null;
	}
}
