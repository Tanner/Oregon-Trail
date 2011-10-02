package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

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
	
	private final static int PADDING = 10;
	
	/**
	 * Constructs a new TextField
	 * @param container Container which holds the text field
	 * @param font Font for the text
	 * @param position Position of the text field
	 * @param width Width of the text field
	 * @param height Height of the text field
	 */
	public TextField(GUIContext container, Font font, Vector2f position, int width, int height) {
		super(container);
		
		fieldColor = Color.gray;
		label = new Label(container, new Vector2f(0, 0), font, Color.white);
		
		this.width = width + 2 * PADDING;
		this.height = height + 2 * PADDING;
		
		setLocation((int)position.getX(), (int)position.getY());
	}
	
	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.setColor(fieldColor);
		g.fillRect(position.getX(), position.getY(), width, height);
		
		label.render(container, g);
	}
	
	/**
	 * Sets the color of the text field
	 * @param color New color
	 */
	public void setFieldColor(Color color) {
		fieldColor = color;
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
			label.setLocation(x + PADDING, y + PADDING);
		}
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
		label.setWidth(width);
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
		label.setHeight(height);
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
		return (int)position.getX();
	}
	
	/**
	 * Get the text entered in this text field.
	 * @return Text in the text field
	 */
	public String getText() {
		return label.getText();
	}
}
