package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

import core.ConstantStore;
import core.Logger;

/**
 * {@code Textfield} inherits from {@code Component} to extend features
 * that let the user enter in text.
 */
public class TextField extends Component implements Disableable {
	private static final int PADDING = 10;

	private Label label;
	private Color fieldColor;
	private Color fieldFocusColor;
	private Color fieldBorderColor;
	private Color fieldFocusBorderColor;
	
	private boolean disabled;
	private String placeholderText;
	
	public enum AcceptedCharacters { LETTERS, LETTERS_NUMBERS, NUMBERS };
	private AcceptedCharacters acceptedCharacters = AcceptedCharacters.LETTERS;
		
	/**
	 * Constructs a {@code TextField} with a font, width, and height.
	 * @param container Container which holds the text field
	 * @param font Font for the text
	 * @param width Width of the text field
	 * @param height Height of the text field
	 */
	public TextField(GUIContext container, int width, int height, Font font) {
		super(container, width, height);
		
		fieldColor = ConstantStore.COLORS.get("INTERACTIVE_NORMAL");
		fieldFocusColor = ConstantStore.COLORS.get("INTERACTIVE_ACTIVE");
		fieldBorderColor = ConstantStore.COLORS.get("INTERACTIVE_BORDER_LIGHT");
		fieldFocusBorderColor = ConstantStore.COLORS.get("INTERACTIVE_BORDER_FOCUS_LIGHT");
		
		setBackgroundColor(fieldColor);
		setBorderColor(Color.white);
		setBorderWidth(2);
		
		label = new Label(container, width - PADDING * 2, font, Color.white);
		add(label, getPosition(Positionable.ReferencePoint.CenterLeft), Positionable.ReferencePoint.CenterLeft, PADDING, 0);
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (!isVisible()) {
			return;
		}
		
		super.render(container, g);
	}
	
	
	@Override
	public void keyReleased(int key, char c) {
		if (hasFocus()) {
			if (isEmpty()) {
				label.setText("");
			}
			
			if (key == Input.KEY_ENTER) {				
				setFocus(false);
			} else if (key == Input.KEY_BACK && label.getText().length() >= 1) {
				Logger.log("Deleting last character", Logger.Level.DEBUG);
				
				label.setText(label.getText().substring(0, label.getText().length() - 1));
			} else if (isAcceptedCharacter(c) && label.getFontWidth() < label.getWidth() - 1) {
				Logger.log("Key pressed is accepted", Logger.Level.DEBUG);
				
				label.setText(label.getText() + c);
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if (!isVisible() || disabled) {
			return;
		}
		
		super.mouseMoved(oldx, oldy, newx, newy);
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		if (isMouseOver()) {
			setFocus(true);
			input.consumeEvent();
		}
	}
	
	/**
	 * Set the placeholder text (text when no user input has been given).
	 * @param text Placeholder text
	 */
	public void setPlaceholderText(String text) {
		placeholderText = text;
		
		if (isEmpty()) {
			label.setText(placeholderText);
		}
	}
	
	/**
	 * Clear the textfield of any text and put the placeholder text there is we're supposed to.
	 */
	public void clear() {
		if (placeholderText != null) {
			label.setText(placeholderText);
		} else {
			label.setText("");
		}
	}
	
	/**
	 * Returns whether the text field is empty.
	 * @return Whether the text field is empty or not
	 */
	public boolean isEmpty() {
		if (label.getText().trim().length() <= 0 || label.getText().equals(placeholderText)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Test to see if a given character is marked as accepted.
	 * @param c Character to test
	 * @return Whether or not the character is allowed to be typed
	 */
	public boolean isAcceptedCharacter(char c) {
		if (acceptedCharacters == AcceptedCharacters.LETTERS) {
			return Character.isLetter(c) || c == ' ';
		} else if (acceptedCharacters == AcceptedCharacters.LETTERS_NUMBERS) {
			return Character.isLetterOrDigit(c) || c == ' ';
		} else if (acceptedCharacters == AcceptedCharacters.NUMBERS) {
			return Character.isDigit(c);
		}
		
		return false;
	}
	
	@Override
	public void setFocus(boolean focus) {
		if (!focus && hasFocus()) {
			if (isEmpty()) {
				label.setText(placeholderText);
			}
			
			notifyListeners();
		}
		
		if (focus) {
			setBackgroundColor(fieldFocusColor);
			setBorderColor(fieldFocusBorderColor);
		} else {
			setBackgroundColor(fieldColor);
			setBorderColor(fieldBorderColor);
		}
		
		super.setFocus(focus);
	}
	
	
	/**
	 * Set the accepted characters for this text field.
	 * @param acceptedCharacters New accepted characters
	 */
	public void setAcceptedCharacters(AcceptedCharacters acceptedCharacters) {
		this.acceptedCharacters = acceptedCharacters;
	}
	
	/**
	 * Sets the color of the text field
	 * @param color New color
	 */
	public void setFieldColor(Color color) {
		fieldColor = color;
	}
	
	public void setFieldFocusColor(Color color) {
		fieldFocusColor = color;
	}
	
	/**
	 * Sets the color of the text
	 * @param color New color
	 */
	public void setTextColor(Color color) {
		label.setColor(color);
	}
	
	/**
	 * Get the text entered in this text field.
	 * @return Text in the text field
	 */
	public String getText() {
		return label.getText().trim();
	}
	
	/**
	 * Set the text for the text field.
	 * @param text Text to be entered
	 */
	public void setText(String text) {
		label.setText(text);
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
