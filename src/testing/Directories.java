package testing;

import filehandler.FileIO;

public class Directories {
	public static String HOME = System.getProperty("user.home") + FileIO.SEP;
	public static String TESTING = HOME + "talkboxtest" + FileIO.SEP;
	public static String RESOURCES = "src" + FileIO.SEP + "testing" + FileIO.SEP;
}
