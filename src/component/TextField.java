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

import core.Logger;

/**
 * Textfield Component that lets the user enter in text
 * 
 * @author Tanner Smith
 */
public class TextField extends Component {
	private Label label;
	private Vector2f position;
	private int width;
	private int height;
	private Color fieldColor;
	private Color fieldFocusColor;
	private boolean over;
	
	public enum AcceptedCharacters { LETTERS, LETTERS_NUMBERS, NUMBERS };
	private AcceptedCharacters acceptedCharacters = AcceptedCharacters.LETTERS;
	
	private final static int PADDING = 10;
	
	/**
	 * Constructs a new TextField
	 * @param container Container which holds the text field
	 * @param font Font for the text
	 * @param width Width of the text field
	 * @param height Height of the text field
	 */
	public TextField(GUIContext container, Font font, int width, int height) {
		super(container);
		
		fieldColor = Color.gray;
		fieldFocusColor = Color.darkGray;
		label = new Label(container, new Vector2f(0, 0), font, Color.white);
		
		setWidth(width);
		setHeight(height);
		
		setLocation((int)position.getX(), (int)position.getY());
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (this.hasFocus()) {
			g.setColor(fieldFocusColor);
		} else {
			g.setColor(fieldColor);
		}
		g.fillRect(position.getX(), position.getY(), width, height);
		
		label.render(container, g);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		if (hasFocus()) {
			if (key == Input.KEY_BACK && label.getText().length() >= 1) {
				Logger.log("Deleting last character", Logger.Level.INFO);
				
				label.setText(label.getText().substring(0, label.getText().length() - 1));
			} else if (isAcceptedCharacter(c)) {
				Logger.log("Key pressed is accepted", Logger.Level.INFO);
				
				label.setText(label.getText() + c);
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		over = getArea().contains(newx, newy);
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {		
		if (over) {
			setFocus(true);
			input.consumeEvent();
		}
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
	
	/**
	 * Get the area of this component.
	 * @return Shape of this component
	 */
	public Shape getArea() {
		return new Rectangle(position.getX(), position.getY(), this.width, this.height);
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
	
	@Override
	public void setLocation(int x, int y) {
		if (position == null) {
			position = new Vector2f(x, y);
		} else {
			position.set(x, y);
		}

		if (label != null) {
			label.setPosition(this.getPosition(Positionable.ReferencePoint.CenterCenter), Positionable.ReferencePoint.CenterCenter);
		}
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
		label.setWidth(width - 2 * PADDING);
		
		System.out.println("label width " + label.getWidth());
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getX() {
		return (int)position.getX();
	}

	@Override
	public int getY() {
		return (int)position.getY();
	}
	
	/**
	 * Get the text entered in this text field.
	 * @return Text in the text field
	 */
	public String getText() {
		return label.getText();
	}
}
