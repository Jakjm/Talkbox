package browsing;

import java.io.File;

/**
 * Interface for listening for when a selection has been clicked for the file
 * selector.
 * 
 * @author jordan
 */
public interface SelectionListener {
	public void onFileSelected(File file);
}
