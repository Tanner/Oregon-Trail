package component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.GUIContext;

public class Button extends Component {
	private Label label;
	private Vector2f position;
	private int width;
	private int height;
	private Color buttonColor;
	
	private final static int PADDING = 10;
	
	public Button(GUIContext container, Label label, Vector2f position, int width, int height) {
		super(container);
		
		this.label = label;
		label.setLocation((int)position.getX() + PADDING, (int)position.getY() + PADDING);
		
		this.position = position;
		this.width = width + 2 * PADDING;
		this.height = height + 2 * PADDING;
		
		buttonColor = Color.gray;
	}
	
	public Button(GUIContext container, Label label, Vector2f position) {
		this(container, label, position, label.getWidth(), label.getHeight());
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		g.setColor(buttonColor);
		g.fillRect(position.getX(), position.getY(), width, height);
		
		label.render(container, g);
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
	}
	
	public void setButtonColor(Color color) {
		buttonColor = color;
	}
}
