package configurer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * An input field for user data.
 * 
 * @author jakjm
 * @version March 26th 2017
 */
public class BasicField extends JPanel {
	private JLabel label;
	private JTextField inputField;

	public BasicField(String fieldName) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		this.label = new JLabel();
		this.label.setText(fieldName);
		this.add(label);

		this.inputField = new JTextField();
		this.add(inputField);
	}

	public BasicField(String fieldName, Color color) {
		this(fieldName);
		this.setBackground(color);
	}

	/**
	 * Disables or enables the field.
	 */
	public void setEnabled(boolean enabled) {
		inputField.setEnabled(enabled);
	}

	/**
	 * Empties the field of text.
	 */
	public void clear() {
		inputField.setText("");
	}

	public void setText(String text) {
		inputField.setText(text);
	}

	/**
	 * Overriding the addKeyListener method of JPanel so that we check when a key is
	 * typed in the search text box.
	 * 
	 */
	public void addKeyListener(KeyListener keyListener) {
		inputField.addKeyListener(keyListener);
	}

	/**
	 * Inserts a string of text at the cursor position
	 */
	public void insertTextAtCursor(String string) {
		int caretPos = inputField.getCaretPosition();
		String currentText = inputField.getText();
		String newText = currentText.substring(0, caretPos) + string + currentText.substring(caretPos);
		inputField.setText(newText);
	}

	/**
	 * Returns the value in the text field.
	 * 
	 * @return the value in the textField.
	 */
	public String getText() {
		return inputField.getText();
	}

	public int parseIntValue() {
		return Integer.parseInt(getText().trim());
	}

	public float parseFloatValue() {
		return Float.parseFloat(getText().trim());
	}

}
