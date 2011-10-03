package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

/**
 * A component that is a Button.
 * 
 * @author Tanner Smith
 */
public class Button extends Component {
	private Label label;
	private Vector2f position;
	private int width;
	private int height;
	private Color buttonColor;
	private Color buttonActiveColor;
	private boolean over;
	private boolean active;
	
	private final static int PADDING = 10;
	
	/**
	 * Creates a button.
	 * @param container Container for the  button
	 * @param label Label for the button
	 * @param position Position of the button
	 * @param width Width of the button
	 * @param height Height of the button
	 */
	public Button(GUIContext container, Label label, Vector2f position, int width, int height) {
		super(container);
		
		this.label = label;
		setLocation((int)position.getX(), (int)position.getY());
		
		this.width = width + 2 * PADDING;
		this.height = height + 2 * PADDING;
				
		buttonColor = Color.gray;
		buttonActiveColor = Color.darkGray;
		
		container.getInput().addMouseListener(this);
	}
	
	/**
	 * Creates a button.
	 * @param container Container for the button
	 * @param label Label for the button
	 * @param position Position of the button
	 */
	public Button(GUIContext container, Label label, Vector2f position) {
		this(container, label, position, label.getWidth(), label.getHeight());
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		if (active) {
			g.setColor(buttonActiveColor);
		} else {
			g.setColor(buttonColor);
		}
		g.fillRect(position.x, position.y, width, height);
		
		label.render(container, g);
	}
	
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		over = getArea().contains(newx, newy);
	}
	
	public void mousePressed(int button, int mx, int my) {
		if (over) {
			active = true;
		}
	}
	
	/**
	 * @see org.newdawn.slick.util.InputAdapter#mouseReleased(int, int, int)
	 */
	public void mouseReleased(int button, int mx, int my) {
		over = getArea().contains(mx, my);
		if (button == 0 && over) {
			notifyListeners();
		} else {
			active = false;
		}
	}

	public Shape getArea() {
		return new Rectangle(position.getX(), position.getY(), this.width, this.height);
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
	
	/**
	 * Sets the color of the button's background.
	 * @param color New color for the button
	 */
	public void setButtonColor(Color color) {
		buttonColor = color;
	}
	
	public void setButtonActiveColor(Color color) {
		buttonActiveColor = color;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
		label.setWidth(width - 2 * PADDING);
	}

	@Override
	public void setHeight(int height) {
		this.height = height;
		label.setHeight(height - 2 * PADDING);
	}
}
