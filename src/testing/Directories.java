package testing;

import filehandler.FileIO;

public class Directories {
	/**TODO this is probably going to make things really hard to keep track of, 
	 * I would recommend changing this. 
	 */
	public static String HOME = System.getProperty("user.home") + FileIO.SEP;
	public static String TESTING = HOME + "talkboxtest";
	public static String RESOURCES = "src" + FileIO.SEP + "testing" + FileIO.SEP;
}
