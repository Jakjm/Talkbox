package filehandler;

import java.io.File;

public class Main {
	private static String homeDir = System.getProperty("user.home");
	private static String testingDir = homeDir + FileIO.SEP + "talkboxtest";
	private static File[] sampleFiles = new File[] { new File("test.wav"), new File("notwave.m4a"),
			new File("image.png"), new File("harbin.jpg") };
	public static void main(String[] args) {
		// create testing directory
		new File(testingDir).mkdirs();
		// creating directory for testing copy methods
		new File(testingDir + FileIO.SEP + "copy").mkdirs();
		// add test files
		for (File f : sampleFiles) {
			FileIO.copyFile(f, testingDir);
		}
		System.out.println(FileIO.checkImageFile("C:\\Users\\Rohan\\git\\Talkbox6\\src\\testing\\harbin.jpg"));
	}
}
