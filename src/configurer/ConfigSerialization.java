package configurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Provides serialization and deserialization for objects and object files.
 * 
 * @author Alberto Mastrofrancesco
 * @author Jordan Malek
 * @author Rohan Talkad
 */
public class ConfigSerialization {
	/**
	 * Serialization configuration instance to an object file and places it in
	 * specified directory.
	 * 
	 * @param Path The directory in string form.
	 * @param cf   The configuration instance.
	 */
	public static void serialize(String path, Configuration cf) {
		File talkBoxData = new File(path);
		try {
			// write object to file
			FileOutputStream fileOut = new FileOutputStream(talkBoxData);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(cf);
			objectOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Deserializes the given serialized configuration file.
	 * 
	 * @param path The path of the configuration file.
	 * @return the Configuration instance.
	 */
	public static Configuration deserialize(String path) {
		File objectFile = new File(path);
		Configuration deserializedConfig = null;
		try {
			// read object in file
			FileInputStream fileIn = new FileInputStream(objectFile);
			ObjectInputStream config = new ObjectInputStream(fileIn);
			deserializedConfig = (Configuration) config.readObject();
			// close object input stream
			config.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return deserializedConfig;
	}
}
